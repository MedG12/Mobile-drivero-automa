package com.automa.data.common

interface Mapper<Response, Model> {
    fun mapFromResponse(response: Response): Model
}