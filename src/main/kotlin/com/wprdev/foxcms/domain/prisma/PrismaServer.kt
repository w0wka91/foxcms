package com.wprdev.foxcms.domain.prisma

import com.wprdev.foxcms.domain.branch.Branch
import com.wprdev.foxcms.domain.project.domain.Project
import io.aexp.nodes.graphql.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class PrismaServer {

    @Value("\${prisma-server.management.url}")
    private lateinit var url: String
    private val requestBuilder: GraphQLRequestEntity.RequestBuilder by lazy {
        GraphQLRequestEntity.Builder().url(url)
    }
    private val logger = LoggerFactory.getLogger(PrismaServer::class.java)


    fun addProject(project: Project): GraphQLResponseEntity<AddProjectPayload> {
        logger.info("Adding new project ${project.generatedName}")

        val addProjectInput = InputObject.Builder<String>()
                .put("name", project.generatedName)
                .put("stage", project.branches.first().name)
                .build()
        val request = requestBuilder
                .arguments(Arguments("addProject", Argument("input", addProjectInput)))
                .request(AddProjectPayload::class.java).build()

        val res = GraphQLTemplate().mutate(request, AddProjectPayload::class.java)
        if (res.errors != null && res.errors.isNotEmpty()) {
            throw IllegalStateException("Couldn't add service: ${res.errors[0].message}")
        }
        return res
    }

    fun deploy(branch: Branch): GraphQLResponseEntity<DeployPayload> {
        logger.info("Deploying branch ${branch.name} for project ${branch.project.generatedName}")

        val deployInput = InputObject.Builder<String>()
                .put("name", branch.project.generatedName)
                .put("stage", branch.name)
                .put("force", true)
                .put("types", branch.generateSDL())
                .build()
        val request = requestBuilder
                .arguments(Arguments("deploy", Argument("input", deployInput)))
                .request(DeployPayload::class.java).build()

        val res = GraphQLTemplate().mutate(request, DeployPayload::class.java)
        if (res.errors != null && res.errors.isNotEmpty()) {
            throw IllegalStateException("Couldn't deploy datamodel: ${res.errors[0].message}")
        }
        return res
    }
}