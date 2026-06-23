package com.akira.miokotoba

import com.akira.miokotoba.data.InMemoryWordBookRepository
import com.akira.miokotoba.data.WordBookRepository

object AppContainer {
    val wordBookRepository: WordBookRepository = InMemoryWordBookRepository()
}