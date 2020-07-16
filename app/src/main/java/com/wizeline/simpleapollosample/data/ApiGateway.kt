package com.wizeline.simpleapollosample.data

import com.wizeline.simpleapollo.exceptions.EmptyResponse
import com.wizeline.simpleapollosample.entities.ApiResponse
import com.wizeline.simpleapollosample.entities.ToDo

interface ApiGateway {
    suspend fun getToDos(): ApiResponse<List<ToDo>>
    suspend fun addToDo(toDo: ToDo): ApiResponse<ToDo>
}
