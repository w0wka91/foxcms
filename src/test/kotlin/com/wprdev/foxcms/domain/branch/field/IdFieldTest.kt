package com.wprdev.foxcms.domain.branch.field

import org.assertj.core.api.Assertions
import org.junit.Test


class IdFieldTest {

    @Test
    fun generateSDL() {
        val idField = IdField
        Assertions.assertThat(IdField.generateSDL()).isEqualTo("id: ID! @id")
    }
}