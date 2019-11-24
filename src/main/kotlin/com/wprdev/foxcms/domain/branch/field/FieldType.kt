package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.BaseEntity
import com.wprdev.foxcms.domain.branch.ModelName

sealed class FieldType(val name: ModelName) : BaseEntity() {
    override fun toString(): String = name.value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldType) return false
        if (this.name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        fun build(displayType: DisplayType): FieldType {
            return when (displayType) {
                DisplayType.SINGLE_LINE_TEXT -> STRING(displayType)
                DisplayType.MULTI_LINE_TEXT -> STRING(displayType)
                DisplayType.INTEGER -> INTEGER()
                DisplayType.FLOAT -> FLOAT()
                DisplayType.CHECKBOX -> BOOLEAN(displayType)
                DisplayType.DATE -> DATETIME(displayType)
                DisplayType.JSON_EDITOR -> JSON(displayType)
                else -> throw IllegalArgumentException("display type: $displayType not supported")
            }
        }
    }

    class STRING(displayType: DisplayType) : FieldType(name = ModelName("String"))
    class INTEGER : FieldType(name = ModelName("Int"))
    class FLOAT : FieldType(name = ModelName("Float"))
    class BOOLEAN(displayType: DisplayType) : FieldType(name = ModelName("Boolean"))
    class DATETIME(displayType: DisplayType) : FieldType(name = ModelName("DateTime"))
    class JSON(displayType: DisplayType) : FieldType(name = ModelName("Json"))
}