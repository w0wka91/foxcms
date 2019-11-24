package com.wprdev.foxcms.domain.prisma

import io.aexp.nodes.graphql.annotations.GraphQLArgument
import io.aexp.nodes.graphql.annotations.GraphQLProperty

@GraphQLProperty(name = "deploy", arguments = [GraphQLArgument(name = "input")])
data class DeployPayload(var clientMutationId: String? = null) {
}