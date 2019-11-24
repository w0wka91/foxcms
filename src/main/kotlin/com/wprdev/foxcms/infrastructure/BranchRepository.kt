package com.wprdev.foxcms.infrastructure

import com.wprdev.foxcms.domain.branch.Branch
import org.springframework.data.repository.CrudRepository

interface BranchRepository : CrudRepository<Branch, Long>