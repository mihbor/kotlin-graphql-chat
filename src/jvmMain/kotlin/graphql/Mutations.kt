package graphql

import com.expediagroup.graphql.server.operations.Mutation
import model.Message
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class Mutations : Mutation {
  fun addMessage(text: String, context: AuthContext): Message {
    println(context.principal)
    if (context.principal == null) {
      throw Exception("Forbidden")
    } else {
      val id = UUID.randomUUID().toString()
      val message = Message(id, text, context.principal.name!!)
      data.messages.add(message)
      return message
    }
  }
}