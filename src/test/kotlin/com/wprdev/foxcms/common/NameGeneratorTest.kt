package com.wprdev.foxcms.common

import org.assertj.core.api.Assertions
import org.junit.Test

class NameGeneratorTest {

    @Test
    fun randomName() {
        Assertions.assertThat(NameGenerator.randomName()).isNotBlank()
    }
}