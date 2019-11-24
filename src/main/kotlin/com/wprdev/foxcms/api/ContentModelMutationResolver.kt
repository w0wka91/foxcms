package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Constraint
import com.wprdev.foxcms.domain.branch.RelationType
import com.wprdev.foxcms.domain.branch.field.*
import com.wprdev.foxcms.domain.prisma.PrismaServer
import com.wprdev.foxcms.infrastructure.ContentModelRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException


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
    ): Field? {
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
            return contentModel.fields.find { it == field }
        }
    }

    data class AddListFieldInput(val modelId: Long,
                                 val fieldName: String,
                                 val apiName: String,
                                 val displayType: DisplayType)

    fun addListField(
            input: AddListFieldInput
    ): Field? {
        with(input) {
            val contentModel = contentModelRepo.findById(modelId).orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND) }

            val field = ListField(
                    Name(fieldName),
                    FieldName(apiName),
                    input.displayType)

            contentModel.addField(field)

            contentModelRepo.save(contentModel)
            prismaServer.deploy(contentModel.branch)
            return contentModel.fields.find { it == field }
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

    fun addRelationField(
            input: AddRelationInput
    ): RelationField? {
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
            return relation
        }
    }

    fun deleteField(
            modelId: Long,
            fieldId: Long
    ): Long? {
        val model = this.contentModelRepo.findById(modelId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        val field = model.findField(fieldId)
        model.deleteField(field)
        contentModelRepo.save(model)
        prismaServer.deploy(model.branch)
        return fieldId
    }
}