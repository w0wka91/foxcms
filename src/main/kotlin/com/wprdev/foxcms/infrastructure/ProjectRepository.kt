package com.wprdev.foxcms.infrastructure

import com.wprdev.foxcms.domain.project.Project
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ProjectRepository : CrudRepository<Project, Long> {
    fun findByGeneratedName(generatedName: String): Optional<Project>
}