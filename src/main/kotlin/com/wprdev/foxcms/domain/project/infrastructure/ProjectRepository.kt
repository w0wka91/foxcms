package com.wprdev.foxcms.domain.project.infrastructure

import com.wprdev.foxcms.domain.project.domain.Project
import org.springframework.data.repository.CrudRepository

interface ProjectRepository : CrudRepository<Project, Long> {
    fun findByGeneratedName(generatedName: String): Project?
}