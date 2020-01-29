package com.wprdev.foxcms.domain.branch

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.field.*
import com.wprdev.foxcms.domain.project.Project
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

        val publishStatus = contentModel.fields.stream().filter { f -> f.apiName.value == "status" }.findAny()
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
        assertThat(field.position).isEqualTo(1)
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
    fun `test deleting a field should reorder existing fields`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val field = ScalarField(Name("Field"),
                FieldName("field"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        val field2 = ScalarField(Name("Field2"),
                FieldName("field2"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        val field3 = ScalarField(Name("Field3"),
                FieldName("field3"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.REQUIRED,
                Constraint.UNIQUE)
        contentModel.addField(field)
        contentModel.addField(field2)
        contentModel.addField(field3)

        contentModel.deleteField(field2)

        assertThat(field.position).isEqualTo(1)
        assertThat(field3.position).isEqualTo(2)
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
    fun `test reordering a field upwards`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val name = ScalarField(
                Name("name"),
                FieldName("name"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val birthday = ScalarField(
                Name("birthday"),
                FieldName("birthday"),
                DisplayType.DATE,
                Concern.OPTIONAL,
                Constraint.NONE)
        val street = ScalarField(
                Name("street"),
                FieldName("street"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val streetNo = ScalarField(
                Name("streetNo"),
                FieldName("streetNo"),
                DisplayType.INTEGER,
                Concern.OPTIONAL,
                Constraint.NONE)
        contentModel.addField(name)
        contentModel.addField(birthday)
        contentModel.addField(street)
        contentModel.addField(streetNo)

        contentModel.reorderField(4, 1)

        assertThat(streetNo.position).isEqualTo(1)
        assertThat(name.position).isEqualTo(2)
        assertThat(birthday.position).isEqualTo(3)
        assertThat(street.position).isEqualTo(4)
    }

    @Test
    fun `test reordering a field downwards`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val name = ScalarField(
                Name("name"),
                FieldName("name"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val birthday = ScalarField(
                Name("birthday"),
                FieldName("birthday"),
                DisplayType.DATE,
                Concern.OPTIONAL,
                Constraint.NONE)
        val street = ScalarField(
                Name("street"),
                FieldName("street"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val streetNo = ScalarField(
                Name("streetNo"),
                FieldName("streetNo"),
                DisplayType.INTEGER,
                Concern.OPTIONAL,
                Constraint.NONE)
        contentModel.addField(name)
        contentModel.addField(birthday)
        contentModel.addField(street)
        contentModel.addField(streetNo)

        contentModel.reorderField(2, 4)

        assertThat(name.position).isEqualTo(1)
        assertThat(street.position).isEqualTo(2)
        assertThat(streetNo.position).isEqualTo(3)
        assertThat(birthday.position).isEqualTo(4)
    }

    @Test
    fun `test reordering a field with invalid positions`() {
        val contentModel = ContentModel(Name("User"), ModelName("User"), "")
        val name = ScalarField(
                Name("name"),
                FieldName("name"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val birthday = ScalarField(
                Name("birthday"),
                FieldName("birthday"),
                DisplayType.DATE,
                Concern.OPTIONAL,
                Constraint.NONE)
        val street = ScalarField(
                Name("street"),
                FieldName("street"),
                DisplayType.SINGLE_LINE_TEXT,
                Concern.OPTIONAL,
                Constraint.NONE)
        val streetNo = ScalarField(
                Name("streetNo"),
                FieldName("streetNo"),
                DisplayType.INTEGER,
                Concern.OPTIONAL,
                Constraint.NONE)
        contentModel.addField(name)
        contentModel.addField(birthday)
        contentModel.addField(street)
        contentModel.addField(streetNo)
        Assertions.assertThatThrownBy {
            contentModel.reorderField(-1, 4)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("Invalid positions")
        Assertions.assertThatThrownBy {
            contentModel.reorderField(0, 4)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("Invalid positions")
        Assertions.assertThatThrownBy {
            contentModel.reorderField(1, 5)
        }.isInstanceOf(IllegalStateException::class.java).hasMessageContaining("Invalid positions")
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
                .isEqualTo("type User { id: ID! @id createdAt: DateTime! @createdAt updatedAt: DateTime! @updatedAt status: Status! handle: String! @unique name: String }")
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
                .isEqualTo("type User { id: ID! @id createdAt: DateTime! @createdAt updatedAt: DateTime! @updatedAt status: Status! numbers: [Int!]! @scalarList(strategy: RELATION) comments: [String!]! @scalarList(strategy: RELATION) }")
    }

}