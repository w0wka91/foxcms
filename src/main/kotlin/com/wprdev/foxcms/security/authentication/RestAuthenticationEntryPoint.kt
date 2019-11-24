package com.wprdev.foxcms.security.authentication

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(req: HttpServletRequest?, res: HttpServletResponse?, ex: AuthenticationException?) {
        res?.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized"
        )
    }


}