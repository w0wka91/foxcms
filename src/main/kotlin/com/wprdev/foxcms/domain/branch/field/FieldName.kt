package com.wprdev.foxcms.domain.branch.field

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class FieldName(
        @Column(name = "apiName")
        val value: String) {
    init {
        require(value.isNotEmpty()) { "Name can't be empty" }
        require(value.matches(Regex("^[a-zA-Z0-9_]*\$"))) { "Name has to be alphanumeric" }
        require(value.length <= 64) { "Name can contain at most 64 characters" }
        require(!Character.isUpperCase(value[0])) { "Name needs to start with an lowercase letter" }
    }

    operator fun plus(anotherString: String) = this.value + anotherString

    override fun toString() = this.value

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is FieldName) return false
        val otherObjectType = other as FieldName?
        return otherObjectType!!.value == this.value
    }
}