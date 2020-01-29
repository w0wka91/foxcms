package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.wprdev.foxcms.domain.project.Project
import com.wprdev.foxcms.domain.project.ProjectService
import com.wprdev.foxcms.infrastructure.ProjectRepository
import org.springframework.stereotype.Component

@Component
class ProjectMutationResolver(
        private val projectService: ProjectService,
        private val projectRepo: ProjectRepository
) : GraphQLMutationResolver {

    fun createProject(name: String): Project {
        return this.projectService.createProject(name)
    }
}