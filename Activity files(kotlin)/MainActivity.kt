package com.example.siteprogress

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory


class MainActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /*
        db = FirebaseFirestore.getInstance()


        // Test Firestore - Add Data
        val project: MutableMap<String, Any> = HashMap()
        project["name"] = "Bridge Construction"
        project["location"] = "Mumbai"
        project["progress"] = "40%"



        db!!.collection("projects")
            .add(project)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Project added: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding project: ${e.localizedMessage}", e)
            }

         */



            FirebaseApp.initializeApp(this)
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, login::class.java))
            finish()
        }, 2000) // 2 seconds delay

    }
}



