package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.BaseEntity
import com.wprdev.foxcms.common.Name
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "\"enum\"")
class Enum(
        @Embedded val name: Name,
        @Embedded val apiName: ModelName,
        values: List<String>) : BaseEntity() {

    @Type(type = "string-array")
    @Column(
            name = "values",
            columnDefinition = "text[]"
    )
    private val _values: Array<String> = values.toTypedArray()
    val values: List<String>
        get() = _values.asList()

    @ManyToOne
    @JoinColumn(name = "branch_id")
    lateinit var branch: Branch

    init {
        val alphanumericRegex = Regex("^[a-zA-Z0-9]*\$")
        values.forEach {
            require(!Character.isLowerCase(it[0])) { "Value needs to start with an uppercase" }
            require(it.matches(alphanumericRegex)) { "Value has to be alphanumeric" }
            require(it.length <= 191) { "Value can contain at most 191 characters" }
        }
    }

    fun generateSDL(): String {
        val sdl = StringBuilder("enum $name")
        sdl.append(" { ")
        for (value in values.distinct()) {
            sdl.append("$value ")
        }
        sdl.append("}")
        return sdl.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Enum) return false
        val otherObjectType = other as Enum?
        return otherObjectType!!.apiName == this.apiName
    }
}