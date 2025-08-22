package com.example.quanlybandienthoai.model.repository

import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.tasks.await

object SlideRepository {
    val db by lazy { Firebase.firestore }
    private val slideCollectionRef by lazy { db.collection("slide") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun insertSlide(slide: Slide) {
//        slide.maSlide = getNextSlideId()
        slideCollectionRef.document(slide.maSlide.toString()).set(slide)
    }

    suspend fun getNextSlideId(): Int {
        val snapshot =
            slideCollectionRef.orderBy("maSlide", Query.Direction.DESCENDING).limit(1).get().await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("maSlide") ?: 0
            (lastId + 1).toInt()
        } else {
            1 // Nếu chưa có sản phẩm nào, bắt đầu từ 1
        }
    }

    suspend fun getAllSlides(): List<Slide> =
        slideCollectionRef.whereNotEqualTo("trash","disable").orderBy("maSlide", Query.Direction.ASCENDING).get().await().toObjects(
            Slide::class.java
        )
}