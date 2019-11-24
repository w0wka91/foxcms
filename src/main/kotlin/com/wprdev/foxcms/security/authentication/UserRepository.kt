package com.wprdev.foxcms.security.authentication

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, String> {
    fun findByUsername(username: String): Optional<User>

}