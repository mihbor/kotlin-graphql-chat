package chat.graphql

import com.expediagroup.graphql.server.operations.Query
import chat.data
import model.Message
import org.springframework.stereotype.Component

@Component
class Queries : Query {
  fun messages(context: SpringAuthContext): List<Message> {
    println(context.subject)
    if (context.subject == null) {
      throw Exception("Forbidden")
    } else {
      return data.messages
    }
  }
}
