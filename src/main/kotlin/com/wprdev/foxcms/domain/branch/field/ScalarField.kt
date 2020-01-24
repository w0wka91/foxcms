package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Constraint
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "field")
@DiscriminatorValue("1")
open class ScalarField(@Embedded
                       override val name: Name,
                       @Embedded
                       override val apiName: FieldName,
                       @Enumerated(EnumType.STRING)
                       @Type(type = "pgsql_enum")
                       open val type: DisplayType,
                       @Enumerated(EnumType.STRING)
                       @Type(type = "pgsql_enum")
                       open val concern: Concern,
                       @Enumerated(EnumType.STRING)
                       @Type(type = "pgsql_enum")
                       @Column(name = "\"constraint\"")
                       open val constraint: Constraint) : FieldEntity() {
    override var position: Int = 0
    override fun generateSDL(): String {
        val type = StringBuilder(this.type.toString())
        if (Concern.REQUIRED == this.concern) {
            type.append("!")
        }
        if (Constraint.UNIQUE == constraint) {
            type.append(" @unique")
        }
        return this.apiName.value + ": " + type
    }
}