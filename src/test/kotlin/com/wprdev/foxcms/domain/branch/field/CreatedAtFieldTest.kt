package com.wprdev.foxcms.domain.branch.field

import org.assertj.core.api.Assertions
import org.junit.Test

class CreatedAtFieldTest {
    @Test
    fun `test generate sdl`() {
        val field = CreatedAtField
        Assertions.assertThat(CreatedAtField.generateSDL()).isEqualTo("createdAt: DateTime! @createdAt")
    }
}