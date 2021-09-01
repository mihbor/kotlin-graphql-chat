package chat.graphql

import com.expediagroup.graphql.server.operations.Mutation
import model.Message
import org.springframework.stereotype.Component
import java.util.UUID
import chat.data

@Component
class Mutations : Mutation {
  fun addMessage(text: String, context: SpringAuthContext): Message {
    println(context.subject)
    if (context.subject == null) {
      throw Exception("Forbidden")
    } else {
      val id = UUID.randomUUID().toString()
      val message = Message(id, text, context.subject!!)
      data.messages.add(message)
      return message
    }
  }
}