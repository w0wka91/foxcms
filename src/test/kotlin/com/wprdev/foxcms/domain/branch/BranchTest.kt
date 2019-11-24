package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.field.EnumField
import com.wprdev.foxcms.domain.branch.field.EnumListField
import com.wprdev.foxcms.domain.branch.field.FieldName
import com.wprdev.foxcms.domain.project.domain.Project
import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BranchTest {

    lateinit var branch: Branch

    @Before
    fun setup() {
        branch = Branch(Project("Test"), "master")
    }

    @Test
    fun `test initial state`() {
        assertTrue(branch.enums.any { it.name.value == "Status" })
    }

    @Test
    fun `test adding enum`() {
        val enumCount = branch.enums.size
        val enum = Enum(Name("TrainType"), ModelName("TrainType"), listOf("RE", "ICE", "IC"))
        branch.addEnum(enum)
        assertEquals(enumCount + 1, branch.enums.size)
        assertNotNull(branch.enums.find { it.name.value == "TrainType" })
    }

    @Test
    fun `test adding a duplicate enum`() {
        val enum = Enum(Name("TrainType"), ModelName("TrainType"), listOf("RE", "ICE", "IC"))
        branch.addEnum(enum)
        val enum2 = Enum(Name("User"), ModelName("User"), listOf("A", "B", "C"))

        val model = ContentModel(
                Name("User"),
                ModelName("User"),
                ""
        )
        branch.addContentModel(model)

        Assertions.assertThatThrownBy {
            branch.addEnum(enum)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already exists")
        Assertions.assertThatThrownBy {
            branch.addEnum(enum2)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already exists")
    }

    @Test
    fun `test remove enum`() {
        val enumCount = branch.enums.size
        val enum = Enum(Name("TrainType"), ModelName("TrainType"), listOf("RE", "ICE", "IC"))
        branch.addEnum(enum)
        assertEquals(enumCount + 1, branch.enums.size)
        branch.deleteEnum(enum)
        assertEquals(enumCount, branch.enums.size)
        assertNull(branch.enums.find { it.name.value == "TrainType" })
    }

    @Test
    fun `test remove enum should delete related fields`() {
        val type = Enum(Name("TrainType"), ModelName("TrainType"), listOf("RE", "ICE", "IC"))
        branch.addEnum(type)

        val model = ContentModel(
                Name("Train"),
                ModelName("Train"),
                ""
        )

        val field = EnumField(
                Name("type"),
                FieldName("type"),
                Concern.OPTIONAL,
                type
        )

        val field2 = EnumListField(
                Name("type2"),
                FieldName("type2"),
                type
        )

        model.addField(field)
        model.addField(field2)
        branch.addContentModel(model)

        val fieldsCount = model.fields.size
        branch.deleteEnum(type)
        assertEquals(fieldsCount - 2, model.fields.size)
        assertEquals(1, branch.enums.size)
    }

    @Test
    fun `test adding content model`() {
        val modelCount = branch.contentModels.size
        val model = ContentModel(
                Name("User"),
                ModelName("User"),
                ""
        )
        branch.addContentModel(model)
        assertEquals(modelCount + 1, branch.enums.size)
        assertNotNull(branch.contentModels.find { it.apiName.value == "User" })
    }

    @Test
    fun `test adding a duplicate content model`() {
        val model = ContentModel(
                Name("Status"),
                ModelName("Status"),
                ""
        )

        val model2 = ContentModel(
                Name("User"),
                ModelName("User"),
                ""
        )
        branch.addContentModel(model2)

        Assertions.assertThatThrownBy {
            branch.addContentModel(model)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already exists")
        Assertions.assertThatThrownBy {
            branch.addContentModel(model2)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already exists")
    }
}