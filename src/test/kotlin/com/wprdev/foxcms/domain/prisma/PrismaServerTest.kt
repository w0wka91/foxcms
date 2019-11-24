package com.wprdev.foxcms.domain.prisma

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.*
import com.wprdev.foxcms.domain.branch.field.DisplayType
import com.wprdev.foxcms.domain.branch.field.FieldName
import com.wprdev.foxcms.domain.branch.field.RelationField
import com.wprdev.foxcms.domain.branch.field.ScalarField
import com.wprdev.foxcms.domain.project.domain.Project
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
class PrismaServerTest {

    @Autowired
    lateinit var prismaServer: PrismaServer

    @Test
    fun `test add project`() {
        val project = Project("Test")
        prismaServer.addProject(project)
    }

    @Test
    fun `test deploy initial`() {
        val project = Project("Test")
        prismaServer.addProject(project)

        prismaServer.deploy(project.branches[0])
    }

    @Test
    fun `test deploy multiple models`() {
        val project = Project("Test")
        prismaServer.addProject(project)
        val branch = project.branches[0]

        val tweet = ContentModel(
                Name("Tweet"),
                ModelName("Tweet"),
                ""
        )
        tweet.addField(
                ScalarField(
                        Name("Text"),
                        FieldName("text"),
                        DisplayType.MULTI_LINE_TEXT,
                        Concern.REQUIRED,
                        Constraint.NONE
                )
        )
        val user = ContentModel(
                Name("User"),
                ModelName("User"),
                ""
        )
        user.addField(
                ScalarField(
                        Name("Handle"),
                        FieldName("handle"),
                        DisplayType.SINGLE_LINE_TEXT,
                        Concern.REQUIRED,
                        Constraint.UNIQUE
                )
        )
        user.addField(
                ScalarField(
                        Name("Name"),
                        FieldName("name"),
                        DisplayType.SINGLE_LINE_TEXT,
                        Concern.OPTIONAL,
                        Constraint.NONE
                )
        )
        val location = ContentModel(
                Name("Location"),
                ModelName("Location"),
                ""
        )
        location.addField(
                ScalarField(
                        Name("Latitude"),
                        FieldName("latitude"),
                        DisplayType.FLOAT,
                        Concern.REQUIRED,
                        Constraint.NONE
                )
        )
        location.addField(
                ScalarField(
                        Name("Longitude"),
                        FieldName("longitude"),
                        DisplayType.FLOAT,
                        Concern.REQUIRED,
                        Constraint.NONE
                )
        )
        branch.addContentModel(tweet)
        branch.addContentModel(user)
        branch.addContentModel(location)
        user.addField(
                RelationField(
                        Name("Tweets"),
                        FieldName("tweets"),
                        tweet,
                        RelationType.ONE_TO_MANY
                )
        )

        prismaServer.deploy(branch)
    }
}