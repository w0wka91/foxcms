package com.wprdev.foxcms.domain.branch.field

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType
import com.wprdev.foxcms.common.Name
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "field")
@TypeDef(name = "pgsql_enum",
        typeClass = PostgreSQLEnumType::class)
@DiscriminatorValue("2")
class ListField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName,
        @Enumerated(EnumType.STRING)
        @Type(type = "pgsql_enum")
        val type: DisplayType) : FieldEntity() {
    override var position: Int = 0
    override fun generateSDL(): String = "${this.apiName.value}: [${this.type}!]! @scalarList(strategy: RELATION)"
}