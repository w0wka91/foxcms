package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.BaseEntity
import com.wprdev.foxcms.domain.branch.ContentModel
import javax.persistence.*

@Entity(name = "UserField")
@Table(name = "field")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        discriminatorType = DiscriminatorType.INTEGER,
        name = "type_id",
        columnDefinition = "TINYINT(1)"
)
abstract class FieldEntity : Field, BaseEntity() {
    @ManyToOne
    @JoinColumn(name = "content_model_id")
    open lateinit var contentModel: ContentModel

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is FieldEntity) return false
        val otherObjectType = other as FieldEntity?
        return otherObjectType!!.apiName == this.apiName
    }
}