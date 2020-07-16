package com.wizeline.simpleapollosample.data

import com.wizeline.simpleapollosample.entities.ApiResponse
import com.wizeline.simpleapollosample.entities.ToDo

class SimpleApolloApiGateway(
    private val simpleApolloApiGateway: SimpleApolloApiGateway
) : ApiGateway {

    override suspend fun getToDos(): ApiResponse<List<ToDo>> {
        TODO("Not yet implemented")
    }

    override suspend fun addToDo(toDo: ToDo): ApiResponse<ToDo> {
        TODO("Not yet implemented")
    }
}
