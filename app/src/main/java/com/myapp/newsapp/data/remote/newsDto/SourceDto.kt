package com.myapp.newsapp.data.remote.newsDto

import com.myapp.newsapp.domain.model.Source

data class SourceDto(
    val id: String,
    val name: String
) {
    fun toSource(): Source {
        return Source(id, name)
    }
}