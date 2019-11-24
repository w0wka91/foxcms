package com.wprdev.foxcms.domain.branch

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ModelName(
        @Column(name = "apiName")
        val value: String) {
    init {
        require(value.isNotEmpty()) { "Name can't be empty" }
        require(value.matches(Regex("^[a-zA-Z0-9 ]*\$"))) { "Name has to be alphanumeric" }
        require(value.length <= 64) { "Name can contain at most 64 characters" }
        require(!Character.isLowerCase(value[0])) { "Name needs to start with an uppercase" }
    }

    operator fun plus(anotherString: String) = this.value + anotherString

    override fun toString() = this.value

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is ModelName) return false
        val otherObjectType = other as ModelName?
        return otherObjectType!!.value == this.value
    }
}