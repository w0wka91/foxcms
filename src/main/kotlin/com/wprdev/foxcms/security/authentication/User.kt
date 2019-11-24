package com.wprdev.foxcms.security.authentication

import com.wprdev.foxcms.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "\"user\"")
class User : BaseEntity {
    val username: String
    val password: String

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}