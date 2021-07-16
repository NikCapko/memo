package com.nikcapko.domain.repository

import com.nikcapko.core.network.Resource
import com.nikcapko.domain.model.DictionaryModel
import com.nikcapko.domain.model.WordModel

interface IDictionaryRepository {
    suspend fun getDictionaryList(): Resource<List<DictionaryModel>>
    suspend fun getDictionary(id: String): Resource<List<WordModel>>
}
