package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.field.*
import com.wprdev.foxcms.domain.project.domain.Project
import junit.framework.Assert.assertTrue
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse

class ContentModelTest {
    @Test
    fun `test initial state`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")

        val idField = contentModel.fields.stream().filter { f -> f.apiName.value == "id" }.findAny()
        assertThat(idField.get()).isOfAnyClassIn(IdField::class.java)

        val createdAt = contentModel.fields.stream().filter { f -> f.apiName.value == "createdAt" }.findAny()
        assertThat(createdAt.get()).isOfAnyClassIn(CreatedAtField::class.java)

        val updatedAt = contentModel.fields.stream().filter { f -> f.apiName.value == "updatedAt" }.findAny()
        assertThat(updatedAt.get()).isOfAnyClassIn(UpdatedAtField::class.java)

        val publishStatus = contentModel.fields.stream().filter { f -> f.apiName.value == "publishStatus" }.findAny()
        assertThat(publishStatus.get()).isOfAnyClassIn(PublishStatusField::class.java)
    }

    @Test
    fun `test adding a scalar field`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val field = ScalarField(Name("Handle"),
                FieldName("handle"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        contentModel.addField(field)
        assertTrue(contentModel.fields.any { it == field })
    }

    @Test
    fun `test deleting a field`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val field = ScalarField(Name("Handle"),
                FieldName("handle"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        contentModel.addField(field)
        assertTrue(contentModel.fields.any { it == field })
        contentModel.deleteField(field)
        assertFalse(contentModel.fields.any { it == field })
    }

    @Test
    fun `test deleting a relation field`() {
        val project = Project("Project")
        val branch = Branch(project, "master")
        val user = ContentModel(Name("User"), ModelName("User"), "")
        val address = ContentModel(Name("Address"), ModelName("Address"), "")
        user.branch = branch
        address.branch = branch

        val addresses = RelationField(
                Name("addresses"),
                FieldName("addresses"),
                address,
                RelationType.ONE_TO_MANY)

        user.addField(addresses)
        user.deleteField(addresses)
        assertThat(user.domainEvents().size).isEqualTo(1)
    }

    @Test
    fun `test deleting a relation field on different branches`() {
        val project = Project("Project")

        val branch = Branch(project, "master")
        val branch2 = Branch(project, "master")

        val user = ContentModel(Name("User"), ModelName("User"), "")
        val address = ContentModel(Name("Address"), ModelName("Address"), "")
        user.branch = branch
        address.branch = branch2

        val addresses = RelationField(
                Name("addresses"),
                FieldName("addresses"),
                address,
                RelationType.ONE_TO_MANY)

        Assertions.assertThatThrownBy {
            user.addField(addresses)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("same branch")
    }

    @Test
    fun `test adding a duplicate field`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val nameField = ScalarField(
                Name("name"),
                FieldName("name"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        contentModel.addField(nameField)
        Assertions.assertThatThrownBy {
            contentModel.addField(nameField)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already exists")
    }

    @Test
    fun `test generateSDL for scalar fields`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val handle = ScalarField(Name("Handle"),
                FieldName("handle"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        val name = ScalarField(Name("Name"),
                FieldName("name"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        contentModel.addField(handle)
        contentModel.addField(name)
        assertThat(contentModel.generateSDL())
                .isEqualTo("type User { id: ID! @id createdAt: DateTime! @createdAt updatedAt: DateTime! @updatedAt publishStatus: PublishStatus! handle: String! @unique name: String }")
    }

    @Test
    fun `test generateSDL for list fields`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val numbers = ListField(Name("Numbers"),
                FieldName("numbers"),
                DisplayType.INTEGER)
        val comments = ListField(Name("Comments"),
                FieldName("comments"),
                DisplayType.SINGLE_LINE_TEXT)
        contentModel.addField(numbers)
        contentModel.addField(comments)
        assertThat(contentModel.generateSDL())
                .isEqualTo("type User { id: ID! @id createdAt: DateTime! @createdAt updatedAt: DateTime! @updatedAt publishStatus: PublishStatus! numbers: [Int!]! @scalarList(strategy: RELATION) comments: [String!]! @scalarList(strategy: RELATION) }")
    }

}