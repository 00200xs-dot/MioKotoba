package com.akira.miokotoba

import android.content.Context
import com.akira.miokotoba.data.RoomWordBookRepository
import com.akira.miokotoba.data.WordBookRepository
import com.akira.miokotoba.data.local.AppDatabase

object AppContainer {
    lateinit var wordBookRepository: WordBookRepository
        private set

    fun initialize(context: Context) {
        if (::wordBookRepository.isInitialized) return

        val database = AppDatabase.getDatabase(context)
        wordBookRepository = RoomWordBookRepository(database.wordBookDao())
    }
}
