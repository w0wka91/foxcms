package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.wprdev.foxcms.domain.project.domain.Project
import com.wprdev.foxcms.domain.project.domain.ProjectService
import org.springframework.stereotype.Component

@Component
class ProjectMutationResolver(
        private val projectService: ProjectService
) : GraphQLMutationResolver {

    fun createProject(name: String): Project {
        return this.projectService.createProject(name)
    }
}