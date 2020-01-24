package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Enum
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "field")
@DiscriminatorValue("4")
class EnumField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName,
        @Enumerated(EnumType.STRING)
        @Type(type = "pgsql_enum")
        val concern: Concern,
        @ManyToOne
        @JoinColumn(name = "enum_id")
        val enum: Enum) : FieldEntity() {
    override var position: Int = 0
    override fun generateSDL(): String {
        val type = StringBuilder(this.enum.apiName.toString())
        if (Concern.REQUIRED == this.concern) {
            type.append("!")
        }
        return this.apiName.value + ": " + type
    }
}