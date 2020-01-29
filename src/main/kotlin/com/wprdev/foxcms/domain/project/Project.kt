package com.wprdev.foxcms.domain.project

import com.wprdev.foxcms.common.BaseEntity
import com.wprdev.foxcms.common.NameGenerator
import com.wprdev.foxcms.domain.branch.Branch
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "project")
class Project : BaseEntity {
    @OneToMany(mappedBy = "project")
    @Cascade(CascadeType.ALL)
    val branches = mutableListOf<Branch>()
    val name: String
    val generatedName: String

    init {
        this.addBranch(Branch(this, "master"))
    }

    constructor(name: String) {
        this.name = name
        this.generatedName = NameGenerator.randomName()
    }

    fun addBranch(branch: Branch) {
        this.branches.add(branch)
    }
}