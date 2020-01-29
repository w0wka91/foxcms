package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.defaultModels.AssetModel
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "field")
@DiscriminatorValue("6")
class AssetField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName,
        @Enumerated(EnumType.STRING)
        @Type(type = "pgsql_enum")
        val concern: Concern) : FieldEntity() {

    override var position: Int = 0
    override fun generateSDL(): String {
        val type = StringBuilder(AssetModel.apiName.toString())
        if (Concern.REQUIRED == this.concern) {
            type.append("!")
        }
        return this.apiName.value + ": " + type
    }

}