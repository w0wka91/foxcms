package com.wprdev.foxcms.domain.branch

import org.assertj.core.api.Assertions
import org.junit.Test

class ModelNameTest {
    @Test
    fun test_constructor() {
        ModelName("Book")
    }

    @Test
    fun test_constructor_startingLowercaseLetter() {
        Assertions.assertThatThrownBy {
            ModelName("user")
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("uppercase")
    }
}