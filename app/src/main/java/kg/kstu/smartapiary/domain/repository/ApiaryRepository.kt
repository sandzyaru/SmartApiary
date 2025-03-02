package kg.kstu.smartapiary.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

/** Репозиторий для работы с данными пасеки */
class ApiaryRepository {
    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "ApiaryRepository"
    }

    /** Получает список пасек пользователя */
    suspend fun getUserHives(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = db.child("users").child(uid).child("hives").get().await()

            // Новый метод получения списка ульев пользователя
            val hives = snapshot.children.mapNotNull { it.getValue(String::class.java) }

            Log.d(TAG, "Исправлено: получены пасеки пользователя: $hives")
            hives
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении пасек: ${e.message}", e)
            emptyList()
        }
    }


    /** Получает список устройств для каждой пасеки */
    suspend fun getHiveDevices(hiveId: String): List<String> {
        return try {
            val snapshot = db.child("hives").child(hiveId).child("devices").get().await()

            // Новый метод получения списка устройств
            val devices = snapshot.children.mapNotNull { it.getValue(String::class.java) }

            Log.d(TAG, "Исправлено: Улей $hiveId содержит устройства: $devices")
            devices
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при получении устройств улья $hiveId: ${e.message}", e)
            emptyList()
        }
    }


    /** Получает данные с датчиков устройств */
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
}

