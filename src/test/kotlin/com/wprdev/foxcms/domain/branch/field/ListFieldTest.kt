package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ListFieldTest {
    @Test
    fun test_generateSDL() {
        val field = ListField(
                Name("User"),
                FieldName("user"),
                DisplayType.SINGLE_LINE_TEXT
        )
        assertThat(field.generateSDL()).isEqualTo("user: [String!]! @scalarList(strategy: RELATION)")
    }
}