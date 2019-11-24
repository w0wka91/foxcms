package com.wprdev.foxcms.domain.branch.field

import org.assertj.core.api.Assertions
import org.junit.Test

class FieldNameTest {

    @Test
    fun test_constructor() {
        FieldName("user")
    }

    @Test
    fun test_constructor_startingUppercaseLetter() {
        Assertions.assertThatThrownBy {
            FieldName("User")
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("lowercase")
    }
}