package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.infrastructure.ContentModelRepository
import org.springframework.stereotype.Component


@Component
class ContentModelQueryResolver(
        private val contentModelRepository: ContentModelRepository
) : GraphQLQueryResolver {

    fun contentModel(modelId: Long): ContentModel? {
        return contentModelRepository.findById(modelId).orElse(null)
    }

}