package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.BaseEntity
import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.field.EnumField
import com.wprdev.foxcms.domain.branch.field.EnumListField
import com.wprdev.foxcms.domain.branch.field.RelationField
import com.wprdev.foxcms.domain.project.domain.Project
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "branch")
class Branch(@ManyToOne
             @JoinColumn(name = "project_id")
             val project: Project, val name: String) : BaseEntity() {

    @OneToMany(mappedBy = "branch")
    @Cascade(CascadeType.ALL)
    private val _enums = mutableListOf<Enum>()

    @get:Transient
    val enums: MutableList<Enum>
        get() = mutableListOf(
                Enum(Name("Status"), ModelName("Status"), listOf("Draft", "Pending", "Published"))
        ).union(_enums).toMutableList()


    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    val contentModels = mutableListOf<ContentModel>()

    fun addEnum(enum: Enum): Enum {
        check(!contentModels.any { it.apiName == enum.apiName }) { "Content model with the API name ${enum.apiName} already exists" }
        check(!enums.any { it == enum }) { "Enum with the name ${enum.apiName} already exists" }
        enum.branch = this
        this._enums.add(enum)
        return enum
    }

    fun deleteEnum(enum: Enum?) {
        contentModels.forEach { contentModel ->
            contentModel.fields
                    .filter {
                        when (it) {
                            is EnumField -> it.enum == enum
                            is EnumListField -> it.enum == enum
                            else -> false
                        }
                    }.forEach {
                        contentModel.deleteField(it)
                    }
        }
        _enums.remove(enum)
    }

    fun addContentModel(model: ContentModel): ContentModel {
        check(!contentModels.any { it == model }) { "Content model with the API name ${model.apiName} already exists" }
        check(!enums.any { it.apiName == model.apiName }) { "Enum with the name ${model.apiName} already exists" }
        model.branch = this
        this.contentModels.add(model)
        return model
    }

    fun findModel(id: Long) = contentModels.find { it.id == id }

    fun findEnum(id: Long) = enums.find { it.id == id }

    fun deleteContentModel(model: ContentModel?) {
        contentModels.forEach { contentModel ->
            contentModel.fields
                    .filterIsInstance<RelationField>()
                    .filter {
                        it.relatesTo == model
                    }.forEach {
                        contentModel.deleteField(it)
                    }
        }
        contentModels.remove(model)
    }

    fun generateSDL(): String {
        val sdl = StringBuilder()
        for (enum in enums) {
            sdl.append(enum.generateSDL()).append(' ')
        }
        for (model in contentModels) {
            sdl.append(model.generateSDL()).append(' ')
        }
        return sdl.toString()
    }
}