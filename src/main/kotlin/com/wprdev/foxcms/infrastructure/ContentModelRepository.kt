package com.wprdev.foxcms.infrastructure

import com.wprdev.foxcms.domain.branch.ContentModel
import org.springframework.data.repository.CrudRepository

interface ContentModelRepository : CrudRepository<ContentModel, Long>