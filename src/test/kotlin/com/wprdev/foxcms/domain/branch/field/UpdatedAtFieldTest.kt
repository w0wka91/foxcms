package com.wprdev.foxcms.domain.branch.field

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class UpdatedAtFieldTest {

    @Test
    fun generateSDL() {
        val updatedAtField = UpdatedAtField
        assertThat(UpdatedAtField.generateSDL()).isEqualTo("updatedAt: DateTime! @updatedAt")
    }
}