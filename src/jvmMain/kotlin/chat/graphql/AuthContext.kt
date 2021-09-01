package chat.graphql

import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContext
import com.expediagroup.graphql.generator.execution.GraphQLContext
import org.springframework.web.reactive.function.server.ServerRequest
import java.security.Principal

interface AuthContext {
  val subject: String?
}
class SpringAuthContext(override val subject: String?, request: ServerRequest): AuthContext, SpringGraphQLContext(request)
class SubscriptionAuthContext(override val subject: String?): AuthContext, GraphQLContext