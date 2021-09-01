package chat.graphql

import chat.jwtSecret
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContextFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import kotlinx.coroutines.reactor.awaitSingleOrNull

/**
 * Custom logic for how this example app should create its context given the [ServerRequest]
 */
@Component
class SpringGraphQLContextFactory : SpringGraphQLContextFactory<SpringAuthContext>() {

    private val verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build()

    override suspend fun generateContext(request: ServerRequest): SpringAuthContext {
        val authHeader = request.headers().header("Authorization").firstOrNull()
        val token = authHeader?.takeIf{ it.startsWith("Bearer ") }?.let{ it.substring("Bearer ".length) }
        val subject = token?.let{ verifier.verify(it) }?.subject
        return SpringAuthContext(subject, request)
    }
}
