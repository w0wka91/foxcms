package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.domain.branch.Enum
import com.wprdev.foxcms.domain.branch.ModelName
import com.wprdev.foxcms.domain.prisma.PrismaServer
import com.wprdev.foxcms.infrastructure.BranchRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException


@Component
class BranchMutationResolver(
        private val branchRepo: BranchRepository,
        private val prismaServer: PrismaServer
) : GraphQLMutationResolver {

    data class AddContentModelInput(
            val branchId: Long,
            val name: String,
            val apiName: String,
            val description: String
    )

    fun addContentModel(
            input: AddContentModelInput
    ): ContentModel? {
        val branch = this.branchRepo.findById(input.branchId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        val contentModel = ContentModel(
                Name(input.name),
                ModelName(input.apiName),
                input.description)

        branch.addContentModel(contentModel)
        this.branchRepo.save(branch)
        this.prismaServer.deploy(branch)
        return branch.contentModels.find { it == contentModel }
    }


    data class AddEnumInput(
            val branchId: Long,
            val name: String,
            val apiName: String,
            val values: List<String>
    )

    fun addEnum(
            input: AddEnumInput
    ): Enum? {
        val branch = this.branchRepo.findById(input.branchId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        val enum = Enum(
                Name(input.name),
                ModelName(input.apiName),
                input.values)

        branch.addEnum(enum)
        this.branchRepo.save(branch)
        this.prismaServer.deploy(branch)
        return branch.enums.find { it == enum }
    }

    fun deleteContentModel(
            branchId: Long,
            modelId: Long
    ): Long? {
        val branch = this.branchRepo.findById(branchId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        val model = branch.findModel(modelId)

        branch.deleteContentModel(model)
        branchRepo.save(branch)
        this.prismaServer.deploy(branch)
        return modelId
    }

    fun deleteEnum(
            branchId: Long,
            enumId: Long
    ): Long? {
        val branch = this.branchRepo.findById(branchId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        val enum = branch.findEnum(enumId)

        branch.deleteEnum(enum)
        branchRepo.save(branch)
        this.prismaServer.deploy(branch)
        return enumId
    }
}