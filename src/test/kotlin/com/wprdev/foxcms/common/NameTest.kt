package com.wprdev.foxcms.common

import org.assertj.core.api.Assertions
import org.junit.Test

class NameTest {
    @Test
    fun test_constructor() {
        Name("Book")
    }

    @Test
    fun test_constructor_emptyName() {
        Assertions.assertThatThrownBy {
            Name("")
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("empty")
    }

    @Test
    fun test_constructor_nonAlphanumericName() {
        Assertions.assertThatThrownBy {
            Name("User!")
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("alphanumeric")
    }

    @Test
    fun test_constructor_moreThan64Chars() {
        Assertions.assertThatThrownBy {
            Name("User".repeat(20))
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("64 characters")
    }

    @Test
    fun test_constructor_64Chars() {
        Name("Kartbahn".repeat(8))
    }

    @Test
    fun plus() {
        val f1 = Name("Book")
        Assertions.assertThat("$f1 : test").isEqualTo("Book : test")
        Assertions.assertThat("test : $f1").isEqualTo("test : Book")
    }

    @Test
    fun testToString() {
        val f1 = Name("Book")
        Assertions.assertThat(f1.toString()).isEqualTo("Book")
    }
}