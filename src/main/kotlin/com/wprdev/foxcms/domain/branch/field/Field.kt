package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name

interface Field {
    val name: Name
    val apiName: FieldName
    var position: Int
    fun generateSDL(): String
}

object IdField : Field {
    override val name = Name("Id")
    override val apiName = FieldName("id")
    override var position = 0
    override fun generateSDL(): String = "${apiName.value}: ID! @id"
}

object UpdatedAtField : Field {
    override val name = Name("Updated at")
    override val apiName = FieldName("updatedAt")
    override var position = 0
    override fun generateSDL(): String = "${apiName.value}: DateTime! @updatedAt"
}

object CreatedAtField : Field {
    override val name = Name("Created at")
    override val apiName = FieldName("createdAt")
    override var position = 0
    override fun generateSDL(): String = "${apiName.value}: DateTime! @createdAt"
}

object PublishStatusField : Field {
    override val name = Name("Status")
    override val apiName = FieldName("status")
    override var position = 0
    override fun generateSDL(): String = "${apiName.value}: Status!"
}