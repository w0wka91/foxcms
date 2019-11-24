package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Enum
import javax.persistence.*

@Entity
@Table(name = "field")
@DiscriminatorValue("5")
class EnumListField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName,
        @ManyToOne
        @JoinColumn(name = "enum_id")
        val enum: Enum) : FieldEntity() {

    override fun generateSDL(): String = "${this.apiName.value}: [${this.enum.apiName}!]! @scalarList(strategy: RELATION)"

}