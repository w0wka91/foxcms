package com.wprdev.foxcms.common

import graphql.GraphQLError
import graphql.servlet.GenericGraphQLError
import graphql.servlet.GraphQLErrorHandler
import org.springframework.stereotype.Component

@Component
class CustomGraphQlErrorHandler : GraphQLErrorHandler {

    override fun processErrors(errors: MutableList<GraphQLError>?) =
            errors?.map { GenericGraphQLError(it.message) }?.toMutableList() ?: mutableListOf()

}