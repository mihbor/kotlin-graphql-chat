package graphql

import com.expediagroup.graphql.server.operations.Mutation
import model.Message
import java.util.UUID

class Mutations : Mutation {
  fun addMessage(text: String, context: AuthContext): Message {
    println(context.principal)
    if (context.principal == null) {
      throw Exception("Forbidden")
    } else {
      val id = UUID.randomUUID().toString()
      val message = Message(id, text, context.principal.subject!!)
      data.messages.add(message)
      return message
    }
  }
}