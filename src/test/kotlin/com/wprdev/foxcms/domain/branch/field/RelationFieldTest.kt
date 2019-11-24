package com.wprdev.foxcms.domain.branch.field

import com.wprdev.foxcms.common.Name
import com.wprdev.foxcms.domain.branch.ContentModel
import com.wprdev.foxcms.domain.branch.ModelName
import com.wprdev.foxcms.domain.branch.RelationType
import org.assertj.core.api.Assertions
import org.junit.Test

class RelationFieldTest {

    @Test
    fun generateSDL_oneToOne() {
        val user = ContentModel(Name("User"), ModelName("User"), "")
        val relation = RelationField(Name("User"), FieldName("user"), user, RelationType.ONE_TO_ONE)
        Assertions.assertThat(relation.generateSDL())
                .isEqualTo("user: User!")
    }


    @Test
    fun generateSDL_oneToOneDirective() {
        val user = ContentModel(Name("User"), ModelName("User"), "")
        val relation = RelationField(Name("User"), FieldName("user"), user, RelationType.ONE_TO_ONE_DIRECTIVE)
        Assertions.assertThat(relation.generateSDL())
                .isEqualTo("user: User! @relation(link: INLINE)")
    }

    @Test
    fun generateSDL_oneToMany() {
        val tweet = ContentModel(Name("Tweet"), ModelName("Tweet"), "")
        val relation = RelationField(Name("Tweets"), FieldName("tweets"), tweet, RelationType.ONE_TO_MANY)
        Assertions.assertThat(relation.generateSDL())
                .isEqualTo("tweets: [Tweet!]!")
    }

    @Test
    fun generateSDL_manyToOne() {
        val user = ContentModel(Name("User"), ModelName("User"), "")
        val relation = RelationField(Name("User"), FieldName("user"), user, RelationType.MANY_TO_ONE)
        Assertions.assertThat(relation.generateSDL())
                .isEqualTo("user: User! @relation(link: INLINE)")
    }
}