package com.example.vocabularygame
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vocabularygame.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth


    private fun counter()
    {
        var db = DatabaseControl()

        var dr = db.getCounter("users")
        dr.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                     val ss = document.toObject<DatabaseControl.DocStats>()

                    Toast.makeText(this,"count: ${ss?.count}",Toast.LENGTH_SHORT).show()
                } else {
                    print( "No such document")
                }
            }
            .addOnFailureListener { exception ->
                print( "get failed with " + exception)
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            validateData()

        }
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.deneme.setOnClickListener {

             counter()
        //getData("8z5VfENM6Eebob4PBCOxCx1QlQN2")

        }
    }
    private fun getData(kullaniciId:String){
        var db2 = DatabaseControl()
        var sr = db2.getKullanicilar("users",kullaniciId)
        sr.get().addOnSuccessListener { document ->
            if (document != null) {
                var veri = (document.toObject<DatabaseControl.Kullanici>())
                Toast.makeText(this," veri ${veri?.name}",Toast.LENGTH_SHORT).show()

            }

        }

    }
    private fun addFireStoreData(auth_uid:String) {
        val email:String =binding.email.text.toString()
        val password:String =binding.registerPassword.text.toString()
        val name:String = binding.registerName.text.toString()
        var db = Firebase.firestore
        val user = hashMapOf(
            "email" to email,
            "password" to password,
            "name" to name
        )
        db.collection("users").document(auth_uid).set(user)
            .addOnSuccessListener {
                Toast.makeText(this,"user collaction adding successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"You are failed",Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateData() {
        val email:String =binding.email.text.toString().trim{it<= ' '}
        val password:String =binding.registerPassword.text.toString().trim{it<= ' '}
        val name:String = binding.registerName.text.toString()



        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(
              OnCompleteListener <AuthResult> {task->
                    if (task.isSuccessful){

                        val firebaseUser:FirebaseUser = task.result!!.user!!

                        Toast.makeText(this,"You are registered successfully autotantication",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,ProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user_id",firebaseUser.uid)
                        intent.putExtra("email_id",email)
                        addFireStoreData(firebaseUser.uid)
                        startActivity(intent)
                        finish()
                    }
                  else{
                      Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }

                }
            )
    }
}