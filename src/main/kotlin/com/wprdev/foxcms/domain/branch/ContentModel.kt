package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.Aggregate
import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.events.ContentModelEvent
import com.wprdev.foxcms.domain.branch.events.RelationFieldDeleted
import com.wprdev.foxcms.domain.branch.field.*
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*


@Entity
@Table(name = "content_model")
open class ContentModel(@Embedded val name: Name,
                        @Embedded val apiName: ModelName,
                        val description: String) : Aggregate<ContentModelEvent>() {
    @ManyToOne
    @JoinColumn(name = "branch_id")
    lateinit var branch: Branch

    @OneToMany(mappedBy = "contentModel", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @OrderBy("position ASC")
    private val _fields = mutableListOf<FieldEntity>()

    @OneToOne
    @JoinColumn(name = "preview_field_id", referencedColumnName = "id")
    private var _previewField: ScalarField? = null

    val previewField: Field
        get() = _previewField ?: IdField

    @get:Transient
    val fields: MutableList<Field>
        get() =
            mutableListOf(
                    IdField,
                    CreatedAtField,
                    UpdatedAtField,
                    PublishStatusField
            ).union(_fields).toMutableList()

    fun addField(field: FieldEntity) {
        check(!fields.any { it == field }) { "Field already exists" }
        if (field is RelationField) {
            check(branch == field.relatesTo.branch) { "Related model is not in the same branch" }
            check(!fields.any {
                it is RelationField &&
                        it.relatesTo == field.relatesTo
            }) { "There is already a relation between these two models" }
        }
        if (field is ScalarField && field.type == DisplayType.SINGLE_LINE_TEXT && this._previewField == null) {
            this._previewField = field
        }
        field.contentModel = this
        field.position = _fields.size + 1
        this._fields.add(field)
    }

    fun findField(id: Long) = _fields.find { it.id == id }

    fun deleteField(field: Field?) {
        if (field is RelationField) {
            registerEvent(RelationFieldDeleted(id, field.relatesTo.id, field.type))
        }
        _fields.remove(field)
        _fields.forEachIndexed { index, field -> field.position = index + 1 }
    }

    fun reorderField(from: Int, to: Int) {
        check(from > 0 && to > 0 && to <= _fields.size) { "Invalid positions" }
        val removed = _fields.removeAt(from - 1)
        _fields.add(to - 1, removed)
        _fields.forEachIndexed { index, field -> field.position = index + 1 }
    }

    fun generateSDL(): String {
        val sdl = StringBuilder("type $name")
        sdl.append(" {").append(' ')
        for (field in fields) {
            sdl.append(field.generateSDL()).append(' ')
        }
        sdl.append('}')
        return sdl.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is ContentModel) return false
        val otherObjectType = other as ContentModel?
        return otherObjectType!!.apiName == this.apiName
    }
}
