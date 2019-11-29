package com.wprdev.foxcms.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthSuccessHandler(private val mapper: ObjectMapper) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication
    ) {
        response.status = 200
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        mapper.writeValue(
                response.writer,
                UserDto(authentication.name)
        )
    }
}