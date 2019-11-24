package com.wprdev.foxcms.security.authentication


import com.wprdev.foxcms.domain.project.domain.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
class AuthController(
        private val authManager: AuthenticationManager,
        private val userRepo: UserRepository,
        private val encoder: PasswordEncoder,
        private val projectService: ProjectService
) {

    @PostMapping("/register")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestParam(name = "username") username: String, @RequestParam password: String) {
        if (!"""^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\-_?&])(?=\S+$).{8,}""".toRegex().matches(password)) {
            throw ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "The password doesn't meet the security requirements"
            )
        }
        if (this.userRepo.findByUsername(username).isPresent) {
            throw ResponseStatusException(
                    HttpStatus.CONFLICT, "User with that username already exists"
            )
        }
        val user = User(username, encoder.encode(password))
        this.userRepo.save(user)

        val authReq = UsernamePasswordAuthenticationToken(user.username, password)
        val auth = authManager.authenticate(authReq)
        SecurityContextHolder.getContext().authentication = auth

        this.projectService.createProject("Initial project")
    }

    @GetMapping("/username")
    @ResponseBody
    fun currentUsername(auth: Authentication) = auth.principal

}