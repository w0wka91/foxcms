package com.wprdev.foxcms.security.authentication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserDetails : UserDetailsService {
    @Autowired
    private lateinit var userRepo: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = this.userRepo.findByUsername(username)
        if (!user.isPresent) {
            throw UsernameNotFoundException("User not found by name : $username")
        }
        return User
                .withUsername(user.get().username)
                .password(user.get().password)
                .roles("USER")
                .build()
    }

}