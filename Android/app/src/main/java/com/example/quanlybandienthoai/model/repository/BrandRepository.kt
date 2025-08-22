package com.example.quanlybandienthoai.model.repository

import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.tasks.await

object BrandRepository {
    val db by lazy { Firebase.firestore }
    private val categoryCollectionRef by lazy { db.collection("thuonghieu") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun insertCategory(category: Thuonghieu) {
//        category.mathuonghieu = getNextBrandId()
        categoryCollectionRef.document(category.mathuonghieu.toString()).set(category)
    }

    suspend fun getNextBrandId(): Int {
        val snapshot =
            categoryCollectionRef.orderBy("mathuonghieu", Query.Direction.DESCENDING).limit(1).get()
                .await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("mathuonghieu") ?: 0
            (lastId + 1).toInt()
        } else {
            1 // Nếu chưa có sản phẩm nào, bắt đầu từ 1
        }
    }

    suspend fun getAllCategories(): List<Thuonghieu> =
        categoryCollectionRef.whereNotEqualTo("trash", "disable")
            .orderBy("mathuonghieu", Query.Direction.ASCENDING).get().await().toObjects(
            Thuonghieu::class.java
        )

    suspend fun getCategory(id: Int): Thuonghieu? {
        val result = categoryCollectionRef
            .whereEqualTo("mathuonghieu", id)
            .get()
            .await()
            .toObjects(Thuonghieu::class.java)

        return result.firstOrNull()
    }

}