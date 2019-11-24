package com.wprdev.foxcms.domain.project.domain

import com.wprdev.foxcms.domain.prisma.PrismaServer
import com.wprdev.foxcms.domain.project.infrastructure.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(
        private val projectRepo: ProjectRepository,
        private val prismaServer: PrismaServer
) {
    fun createProject(name: String): Project {
        val project = Project(name)
        prismaServer.addProject(project)
        return projectRepo.save(project)
    }
}