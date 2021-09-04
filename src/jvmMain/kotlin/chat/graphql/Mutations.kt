package chat.graphql

import chat.Messages
import chat.data
import model.Message
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

@Component
class Mutations(@Autowired val messages: Messages) : Mutation {
  fun addMessage(text: String, context: SpringAuthContext): Message {
    println("mutation subject: ${context.subject}")
    if (context.subject == null) {
      throw Exception("Forbidden")
    } else {
      val id = UUID.randomUUID().toString()
      val message = Message(id, text, context.subject!!)
      messages.emit?.invoke(message)
      data.messages.add(message)
      return message
    }
  }
}