package com.wprdev.foxcms.domain.branch.field

enum class DisplayType(@Transient val type: String) {
    SINGLE_LINE_TEXT("String"),
    MULTI_LINE_TEXT("String"),
    INTEGER("Int"),
    FLOAT("Float"),
    CHECKBOX("Boolean"),
    DATE("DateTime"),
    IMAGE("Asset"),
    JSON_EDITOR("Json");

    override fun toString() = type
}
