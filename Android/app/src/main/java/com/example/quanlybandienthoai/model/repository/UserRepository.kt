package com.example.quanlybandienthoai.model.repository

//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.firestore.ktx.firestoreSettings
//import com.google.firebase.ktx.Firebase
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.example.quanlybandienthoai.model.remote.entity.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestoreSettings
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object UserRepository {
    //    val db by lazy { Firebase.firestore }
    @SuppressLint("StaticFieldLeak")
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val firebaseAuth = FirebaseAuth.getInstance()

    // creating a collection reference for our Firebase Firestore database.
    private val userCollectionRef: CollectionReference = db.collection("user")

    //Bật bộ nhớ cache offline của Firestore,
    init {
        val settings = firestoreSettings { var isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getUserById(userId: String): User? =
        userCollectionRef.document(userId).get().await().toObject(User::class.java)

    suspend fun getAllUser(): List<User> {
        return try {
            val snapshot = userCollectionRef.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error fetching users: ${e.message}", e)
            emptyList()
        }
    }


    suspend fun getUserByEmail(email: String): User? {
        val users =
            userCollectionRef.whereEqualTo("email", email).get().await().toObjects(User::class.java)
        return if (users.isEmpty()) null
        else users[0]
    }

    suspend fun updateUserStatusById(id: Int, newStatus: String): Boolean {
        val querySnapshot = userCollectionRef
            .whereEqualTo("id", id)
            .get()
            .await()

        if (querySnapshot.isEmpty) return false

        // Lấy document ID trực tiếp từ kết quả truy vấn
        val userDocId = querySnapshot.documents[0].id

        // Tiến hành cập nhật status
        userCollectionRef.document(userDocId)
            .update("status", newStatus)
            .await()

        return true
    }

    suspend fun deleteUserById(id: Int): Boolean {
        val querySnapshot = userCollectionRef
            .whereEqualTo("id", id)
            .get()
            .await()

        if (querySnapshot.isEmpty) return false

        // Lấy document ID của user cần xóa
        val userDocId = querySnapshot.documents[0].id

        // Tiến hành xóa
        userCollectionRef.document(userDocId)
            .delete()
            .await()

        return true
    }

    suspend fun isEmailLocked(email: String): Boolean {
        return try {
            val users = userCollectionRef
                .whereEqualTo("email", email)
                .whereEqualTo("status", "lock")
                .get()
                .await()

            // Nếu có ít nhất một user có status là "lock", trả về true
            !users.isEmpty
        } catch (e: Exception) {
            println("Lỗi khi kiểm tra email: ${e.message}")
            false
        }
    }


    suspend fun getUserById(id: Int): User? {
        val users = userCollectionRef
            .whereEqualTo("id", id)
            .get()
            .await()
            .toObjects(User::class.java)

        return users.firstOrNull()
    }



    fun insertUser(user: User) {
        userCollectionRef.document(user.id.toString()).set(user)
    }

    fun getNextProductId(callback: (Int) -> Unit) {
        userCollectionRef.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { snapshot ->
                val lastId = if (!snapshot.isEmpty) {
                    snapshot.documents[0].getLong("id") ?: 0
                } else {
                    0
                }
                callback((lastId + 1).toInt())
            }
            .addOnFailureListener {
                callback(1) // Nếu có lỗi, mặc định trả về 1
            }
    }


    fun signInAuthentication(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun registerAuthentication(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

//    fun registerAuthentication(
//        email: String,
//        password: String,
//        onResult: (Boolean, String?, String?) -> Unit // Thêm uid vào callback
//    ) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val uid = firebaseAuth.currentUser?.uid
//                    onResult(true, uid, null)
//                } else {
//                    onResult(false, null, task.exception?.message)
//                }
//            }
//    }

    suspend fun registerAuthentication(email: String, password: String): Result<String> = suspendCoroutine { cont ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid
                    if (uid != null) {
                        cont.resume(Result.success(uid))
                    } else {
                        cont.resume(Result.failure(Exception("Không lấy được UID")))
                    }
                } else {
                    cont.resume(Result.failure(task.exception ?: Exception("Đăng ký thất bại")))
                }
            }
    }



    fun sendEmailResetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

}



