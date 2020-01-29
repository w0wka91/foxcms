package com.wprdev.foxcms.domain.project

import com.wprdev.foxcms.domain.prisma.PrismaServer
import com.wprdev.foxcms.infrastructure.ProjectRepository
import org.springframework.stereotype.Service

@Service
class ProjectService(
        private val projectRepo: ProjectRepository,
        private val prismaServer: PrismaServer
) {
    fun createProject(name: String): Project {
        val project = Project(name)
        prismaServer.addProject(project)
        prismaServer.deploy(project.branches.first())
        return projectRepo.save(project)
    }
}