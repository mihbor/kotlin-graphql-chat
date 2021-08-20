package graphql

import com.expediagroup.graphql.server.spring.execution.SpringGraphQLContext
import org.springframework.web.reactive.function.server.ServerRequest
import java.security.Principal

class AuthContext(
  request: ServerRequest,
  val principal: Principal?
) : SpringGraphQLContext(request)