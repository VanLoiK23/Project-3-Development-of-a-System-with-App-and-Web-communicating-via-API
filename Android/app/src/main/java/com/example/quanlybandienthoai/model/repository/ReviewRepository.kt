package com.example.quanlybandienthoai.model.repository

import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.tasks.await
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath

object ReviewRepository {
    val db by lazy { Firebase.firestore }
    private val reviewCollectionRef by lazy { db.collection("review") }
    private val userCollectionRef: CollectionReference = UserRepository.db.collection("user")

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun insertReview(review: Review) {
//        review.id = getNextReviewId()
        reviewCollectionRef.document(review.id.toString()).set(review).await()
    }

    suspend fun insertReviewFireBase(review: Review) {
        reviewCollectionRef.document(review.id.toString()).set(review).await()
    }


    suspend fun delete(id: Int) {
        reviewCollectionRef.document(id.toString()).delete().await()
    }

    suspend fun getNextReviewId(): Int {
        val snapshot =
            reviewCollectionRef.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                .await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("id") ?: 0
            (lastId + 1).toInt()
        } else {
            1 // Nếu chưa có sản phẩm nào, bắt đầu từ 1
        }
    }

    suspend fun getReview(idSP: Int, orderId: Int): Review? {
        val snapshot = reviewCollectionRef
            .whereEqualTo("order_id", orderId)
            .whereEqualTo("id_sp", idSP)
            .limit(1)
            .get()
            .await()
            .toObjects(Review::class.java)

        return snapshot.firstOrNull()
    }


    suspend fun getAllReviewsWithUsers(id: Int): List<Review> {
        val reviews = reviewCollectionRef.whereEqualTo("id_sp", id)
            .orderBy("ngay_đanhgia", Query.Direction.ASCENDING)
            .get()
            .await()
            .toObjects(Review::class.java)

        // Lấy toàn bộ user ID trong danh sách đánh giá
        val userIds = reviews.map { it.id_user.toString() }.distinct()

        val userMap = if (userIds.isNotEmpty()) {
            val usersSnapshot = userCollectionRef
                .whereIn(FieldPath.documentId(), userIds)
                .get()
                .await()
            usersSnapshot.documents.associate {
                it.id to ((it.getString("lastName") + it.getString("firstName")) ?: "Ẩn danh")
            }
        } else {
            emptyMap()
        }

        return reviews.map { review ->
            review.copy(user = userMap[review.id_user.toString()] ?: "Ẩn danh")
        }
    }


}