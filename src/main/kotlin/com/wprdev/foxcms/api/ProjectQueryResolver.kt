package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.wprdev.foxcms.domain.project.domain.Project
import com.wprdev.foxcms.domain.project.infrastructure.ProjectRepository
import org.springframework.stereotype.Component


@Component
class ProjectQueryResolver(
        private val projectRepo: ProjectRepository
) : GraphQLQueryResolver {

    fun projects() = projectRepo.findAll()

    fun project(id: Long?): Project? {
        return if (id !== null) {
            val b = projectRepo.findById(id)
            if (b.isPresent) b.get() else null
        } else {
            projectRepo.findAll().first()
        }
    }

}