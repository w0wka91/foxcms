package com.wprdev.foxcms

import com.coxautodev.graphql.tools.SchemaParserDictionary
import com.wprdev.foxcms.domain.branch.field.ListField
import com.wprdev.foxcms.domain.branch.field.ScalarField
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter


@Configuration
@EnableJpaAuditing
class Configuration {
    @Bean
    fun OpenFilter() = OpenEntityManagerInViewFilter()

    @Bean
    fun dictionary(): SchemaParserDictionary {
        val schemaParserDictionary = SchemaParserDictionary()
        schemaParserDictionary.add("ScalarField", ScalarField::class)
        schemaParserDictionary.add("ListField", ListField::class)
        return schemaParserDictionary
    }
}