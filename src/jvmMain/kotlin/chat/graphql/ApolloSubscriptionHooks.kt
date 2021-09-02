package chat.graphql

import chat.jwtSecret
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.spring.subscriptions.SimpleSubscriptionHooks
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketSession

@Component
class ApolloSubscriptionHooks: SimpleSubscriptionHooks() {
  private val verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build()

  override fun onConnect(
    connectionParams: Map<String, String>,
    session: WebSocketSession,
    graphQLContext: GraphQLContext?
  ): GraphQLContext? {

    val authParam = connectionParams["Authorization"]
    val token = authParam?.takeIf{ it.startsWith("Bearer ") }?.let{ it.substring("Bearer ".length) }
    val subject = token?.let{ verifier.verify(it) }?.subject
    return SubscriptionAuthContext(subject)
  }
}