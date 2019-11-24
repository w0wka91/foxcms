package com.wprdev.foxcms.security.authentication

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class AuthQueryResolver : GraphQLQueryResolver {

    fun username(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication.name == "anonymousUser") null else authentication.name
    }

}