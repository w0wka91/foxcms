package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.domain.branch.RelationType
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "field")
@DiscriminatorValue("3")
class RelationField(
        @Embedded
        override val name: Name,
        @Embedded
        override val apiName: FieldName,
        @ManyToOne
        @JoinColumn(name = "relates_to")
        val relatesTo: ContentModel,
        @Enumerated(EnumType.STRING)
        @Type(type = "pgsql_enum")
        @Column(name = "relation_type")
        val type: RelationType) : FieldEntity() {

    companion object {
        fun oppositeDirection(type: RelationType) = when (type) {
            RelationType.ONE_TO_ONE -> RelationType.ONE_TO_ONE_DIRECTIVE
            RelationType.ONE_TO_ONE_DIRECTIVE -> RelationType.ONE_TO_ONE
            RelationType.MANY_TO_ONE -> RelationType.ONE_TO_MANY
            RelationType.ONE_TO_MANY -> RelationType.MANY_TO_ONE
            RelationType.MANY_TO_MANY -> RelationType.MANY_TO_MANY
        }
    }

    override var position: Int = 0
    override fun generateSDL(): String {
        val sdl = StringBuilder("${apiName.value}: ")
        when (this.type) {
            RelationType.ONE_TO_ONE -> sdl.append("${relatesTo.name.value}")
            RelationType.ONE_TO_ONE_DIRECTIVE, RelationType.MANY_TO_ONE -> sdl.append("${relatesTo.name.value} @relation(link: INLINE)")
            RelationType.ONE_TO_MANY, RelationType.MANY_TO_MANY -> sdl.append("[${relatesTo.name.value}!]!")
        }
        return sdl.toString()
    }
}