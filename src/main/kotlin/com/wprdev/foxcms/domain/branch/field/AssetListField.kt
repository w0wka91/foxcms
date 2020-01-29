package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import javax.persistence.DiscriminatorValue
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "field")
@DiscriminatorValue("7")
class AssetListField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName) : FieldEntity() {
    override var position: Int = 0
    override fun generateSDL(): String = "${this.apiName.value}: [Asset!]!"

}