package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.Name
import org.assertj.core.api.Assertions
import org.junit.Test

class EnumTest {

    @Test
    fun test_constructor() {
        Enum(Name("PublishStatus"), ModelName("PublishStatus"), listOf("Draft", "Published"))
    }

    @Test
    fun test_constructor_DuplicateValue() {
        val enum = Enum(Name("PublishStatus"), ModelName("PublishStatus"), listOf("Draft", "Published", "Draft"))
        Assertions.assertThat(enum.generateSDL()).isEqualTo("enum PublishStatus { Draft Published }")
    }

    @Test
    fun testGenerateSDL() {
        val enum = Enum(Name("PublishStatus"), ModelName("PublishStatus"), listOf("Draft", "Published"))
        Assertions.assertThat(enum.generateSDL()).isEqualTo("enum PublishStatus { Draft Published }")
    }

}