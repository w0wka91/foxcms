package com.wprdev.foxcms.common

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
open class Name(
        @Column(name = "name")
        val value: String) {

    init {
        require(value.isNotEmpty()) { "Name can't be empty" }
        require(value.matches(Regex("^[a-zA-Z0-9 ]*\$"))) { "Name has to be alphanumeric" }
        require(value.length <= 64) { "Name can contain at most 64 characters" }
    }

    operator fun plus(anotherString: String) = this.value + anotherString

    override fun toString() = this.value

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Name) return false
        val otherObjectType = other as Name?
        return otherObjectType!!.value == this.value
    }
}