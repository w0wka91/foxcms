package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name

interface Field {
    val name: Name
    val apiName: FieldName
    fun generateSDL(): String
}

object IdField : Field {
    override val name = Name("Id")
    override val apiName = FieldName("id")
    override fun generateSDL(): String = "${apiName.value}: ID! @id"
}

object UpdatedAtField : Field {
    override val name = Name("Updated at")
    override val apiName = FieldName("updatedAt")
    override fun generateSDL(): String = "${apiName.value}: DateTime! @updatedAt"
}

object CreatedAtField : Field {
    override val name = Name("Created at")
    override val apiName = FieldName("createdAt")
    override fun generateSDL(): String = "${apiName.value}: DateTime! @createdAt"
}

object PublishStatusField : Field {
    override val name = Name("Status")
    override val apiName = FieldName("status")
    override fun generateSDL(): String = "${apiName.value}: Status!"
}