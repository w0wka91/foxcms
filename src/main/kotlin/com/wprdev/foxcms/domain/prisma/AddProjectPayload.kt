package com.wprdev.foxcms.domain.prisma

import io.aexp.nodes.graphql.annotations.GraphQLArgument
import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "addProject", arguments = [GraphQLArgument(name = "input")])
data class AddProjectPayload(var clientMutationId: String? = null) {
}