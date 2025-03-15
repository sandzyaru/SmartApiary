package kg.kstu.smartapiary.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class ApiaryRepository {
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "ApiaryRepository"
    }

    suspend fun getUserHives(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = db.child("users").child(uid).child("hives").get().await()

            val hives = snapshot.children.mapNotNull { it.getValue(String::class.java) }

            Log.d(TAG, "Исправлено: получены пасеки пользователя: $hives")
            hives
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении пасек: ${e.message}", e)
            emptyList()
        }
    }



    suspend fun getHiveDevices(hiveId: String): List<String> {
        return try {
            val snapshot = db.child("hives").child(hiveId).child("devices").get().await()


            val devices = snapshot.children.mapNotNull { it.getValue(String::class.java) }

            Log.d(TAG, "Исправлено: Улей $hiveId содержит устройства: $devices")
            devices
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении устройств улья $hiveId: ${e.message}", e)
            emptyList()
        }
    }



    suspend fun getDeviceData(deviceMac: String): Map<String, String> {
        return try {
            val snapshot = db.child("units").child(deviceMac).get().await()
            val data = snapshot.children.associate { it.key.orEmpty() to (it.getValue(String::class.java) ?: "") }
            Log.d(TAG, "Данные устройства $deviceMac: $data")
            data
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении данных устройства $deviceMac: ${e.message}", e)
            emptyMap()
        }
    }

    suspend fun claimHiveByMac(macAddress: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        return try {
            val unitSnapshot = db.child("units").child(macAddress).get().await()
            if (!unitSnapshot.exists()) {
                Log.e(TAG, "Устройство с MAC-адресом $macAddress не найдено в units")
                return false
            }

            val hiveId = unitSnapshot.child("hive_id").getValue(String::class.java) ?: return false
            val hiveSnapshot = db.child("hives").child(hiveId).get().await()

            val devices = hiveSnapshot.child("devices").children.mapNotNull { it.getValue(String::class.java) }

            if (macAddress in devices) {
                db.child("hives").child(hiveId).child("ownerId").setValue(uid).await()
                db.child("users").child(uid).child("hives").child(hiveId).setValue(hiveId).await()
                Log.d(TAG, "Пасека $hiveId теперь принадлежит пользователю $uid")
                return true
            } else {
                Log.e(TAG, "MAC-адрес не найден в устройстве этой пасеки")
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при проверке устройства $macAddress: ${e.message}", e)
            return false
        }
    }

    suspend fun getGraphicsData(hiveId: String): Map<String, Map<String, Double>> {
        val uid = auth.currentUser?.uid ?: return emptyMap()
        return try {
            val userSnapshot = db.child("users").child(uid).child("hives").get().await()
            val userHives = userSnapshot.children.mapNotNull { it.getValue(String::class.java) }

            if (hiveId !in userHives) {
                Log.e("ApiaryRepository", "Улей $hiveId не принадлежит пользователю $uid")
                return emptyMap()
            }

            val snapshot = db.child("graphics").child(uid).child(hiveId).get().await()
            if (!snapshot.exists()) return emptyMap()

            val dataMap = mutableMapOf<String, Map<String, Double>>()
            val defaultValues = mapOf("mon" to 0.0, "tue" to 0.0, "wed" to 0.0, "thur" to 0.0, "fr" to 0.0, "sat" to 0.0, "sun" to 0.0)

            snapshot.children.forEach { parameter ->
                val values = parameter.children.associate { it.key!! to (it.getValue(String::class.java)?.toDoubleOrNull() ?: 0.0) }
                dataMap[parameter.key!!] = defaultValues + values
            }
            dataMap
        } catch (e: Exception) {
            Log.e("ApiaryRepository", "Ошибка при получении данных graphics для улья $hiveId: ${e.message}", e)
            emptyMap()
        }
    }

}

