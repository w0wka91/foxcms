package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.Concern
import com.wprdev.foxcms.domain.branch.Constraint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ScalarFieldTest {

    @Test
    fun test_generateSDL_requiredField() {
        val field = ScalarField(
                Name("Text"),
                FieldName("text"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.NONE
        )
        assertThat(field.generateSDL()).isEqualTo("text: String!")
    }

    @Test
    fun test_generateSDL_optionalField() {
        val field = ScalarField(
                Name("Text"),
                FieldName("text"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE
        )
        assertThat(field.generateSDL()).isEqualTo("text: String")
    }

    @Test
    fun test_generateSDL_uniqueField() {
        val field = ScalarField(
                Name("Handle"),
                FieldName("handle"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE
        )
        assertThat(field.generateSDL()).isEqualTo("handle: String! @unique")
    }
}