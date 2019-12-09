package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.wprdev.foxcms.domain.project.infrastructure.ProjectRepository
import org.springframework.stereotype.Component


@Component
class ProjectQueryResolver(
        private val projectRepo: ProjectRepository
) : GraphQLQueryResolver {
    fun projects() = projectRepo.findAll()
    fun project(generatedName: String) = projectRepo.findByGeneratedName(generatedName)
}