package com.wprdev.foxcms.api

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.wprdev.foxcms.domain.branch.Branch
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.infrastructure.BranchRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException


@Component
class BranchQueryResolver(
        private val branchRepo: BranchRepository
) : GraphQLQueryResolver {


    fun branch(id: Long): Branch? {
        return branchRepo.findById(id).orElse(null)
    }

    fun contentModels(branchId: Long): List<ContentModel> {
        val branch = branchRepo.findById(branchId).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        return branch.contentModels
    }

}