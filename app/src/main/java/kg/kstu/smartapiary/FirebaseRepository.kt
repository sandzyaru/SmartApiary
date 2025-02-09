package kg.kstu.smartapiary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {

    private val database = FirebaseDatabase.getInstance(BuildConfig.BASE_URL)
    private val _data = MutableLiveData<ApiaryData>()
    val apiaryData: LiveData<ApiaryData> = _data

    fun loadData() {
        database.reference.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val data = ApiaryData(
                    temperature = snapshot.child("temp").getValue(String::class.java) ?: "N/A",
                    humidity = snapshot.child("humidity").getValue(String::class.java) ?: "N/A",
                    signal = snapshot.child("signal").getValue(String::class.java) ?: "N/A",
                    weight = snapshot.child("weight").getValue(String::class.java) ?: "N/A",
                    pressure = snapshot.child("pressure").getValue(String::class.java) ?: "N/A",
                    altitude = snapshot.child("altitude").getValue(String::class.java) ?: "N/A",
                    connect = snapshot.child("connect").getValue(Boolean::class.java) ?: false
                )

                _data.postValue(data)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                println("Firebase error: ${error.message}")
            }
        })
    }
}