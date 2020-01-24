package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Constraint
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.domain.branch.RelationType
import com.wprdev.foxcms.domain.branch.field.*
import com.wprdev.foxcms.domain.prisma.PrismaServer
import com.wprdev.foxcms.infrastructure.ContentModelRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.lang.IllegalArgumentException


@Component
class ContentModelMutationResolver(
        private val contentModelRepo: ContentModelRepository,
        private val prismaServer: PrismaServer
) : GraphQLMutationResolver {

    data class AddPrimitiveFieldInput(val modelId: Long,
                                      val fieldName: String,
                                      val apiName: String,
                                      val displayType: DisplayType,
                                      val concern: Concern,
                                      val constraint: Constraint
    )

    fun addScalarField(
            input: AddPrimitiveFieldInput
    ): ScalarField? {
        with(input) {
            val contentModel = contentModelRepo.findById(modelId).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

            val field = ScalarField(
                    Name(fieldName),
                    FieldName(apiName),
                    input.displayType,
                    concern,
                    constraint)

            contentModel.addField(field)

            contentModelRepo.save(contentModel)
            prismaServer.deploy(contentModel.branch)
            return contentModel.fields.find { it == field } as ScalarField
        }
    }

    data class AddListFieldInput(val modelId: Long,
                                 val fieldName: String,
                                 val apiName: String,
                                 val displayType: DisplayType)

    fun addListField(
            input: AddListFieldInput
    ): ListField? {
        with(input) {
            val contentModel = contentModelRepo.findById(modelId).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

            val field = ListField(
                    Name(fieldName),
                    FieldName(apiName),
                    input.displayType)

            contentModel.addField(field)

            contentModelRepo.save(contentModel)
            prismaServer.deploy(contentModel.branch)
            return contentModel.fields.find { it == field } as ListField
        }
    }

    data class AddRelationInput(
            val modelId: Long,
            val fieldName: String,
            val apiName: String,
            val relatesToModelId: Long,
            val relatesToFieldName: String,
            val relatesToApiName: String,
            val relationType: RelationType
    )

    data class AddRelationFieldPayload(val modelId: Long, val field: RelationField)

    fun addRelationField(
            input: AddRelationInput
    ): List<AddRelationFieldPayload> {
        check(input.modelId !== input.relatesToModelId) { "Recursive relationships are not supported yet" }
        with(input) {
            val model = contentModelRepo.findById(modelId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
            val relatesToModel = contentModelRepo.findById(relatesToModelId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

            val relation = RelationField(
                    Name(fieldName),
                    FieldName(apiName),
                    relatesToModel,
                    relationType
            )

            model.addField(relation)

            val relationOtherSide = RelationField(
                    Name(relatesToFieldName),
                    FieldName(relatesToApiName),
                    model,
                    RelationField.oppositeDirection(relationType)
            )

            relatesToModel.addField(relationOtherSide)
            contentModelRepo.save(relatesToModel)
            contentModelRepo.save(model)
            prismaServer.deploy(model.branch)
            return listOf(AddRelationFieldPayload(modelId, relation), AddRelationFieldPayload(relatesToModelId, relationOtherSide))
        }
    }

    data class DeleteFieldPayload(val modelId: Long, val fieldId: Long?)

    fun deleteField(
            modelId: Long,
            fieldId: Long
    ): List<DeleteFieldPayload> {
        val model = this.contentModelRepo.findById(modelId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        val field = model.findField(fieldId)
        if(field != null) {
            val result = if(field is RelationField) {
                val relatesToField = field.relatesTo.fields.find { it is RelationField && it.relatesTo.id == modelId } as RelationField
                listOf( DeleteFieldPayload(modelId, field.id), DeleteFieldPayload(field.relatesTo.id, relatesToField.id))
            } else {
                listOf( DeleteFieldPayload(modelId, field.id))
            }
            model.deleteField(field)
            contentModelRepo.save(model)
            prismaServer.deploy(model.branch)
            return result
        } else {
            throw IllegalArgumentException("Field doesnt exist within content model")
        }
    }

    fun reorderField(
            modelId: Long,
            from: Int,
            to: Int
    ): ContentModel? {
        val model = this.contentModelRepo.findById(modelId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        model.reorderField(from, to)
        contentModelRepo.save(model)
        prismaServer.deploy(model.branch)
        return model
    }
}