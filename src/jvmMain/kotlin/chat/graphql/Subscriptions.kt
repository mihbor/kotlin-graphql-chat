package chat.graphql

import model.Message
import chat.Messages
import reactor.core.publisher.Flux
import com.expediagroup.graphql.server.operations.Subscription
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

@Component
class Subscriptions(@Autowired val messages: Messages) : Subscription {
  fun messageAdded(context: SubscriptionAuthContext): Flux<Message> {
    println("subscription subject: ${context.subject}")
    if (context.subject == null) {
      throw Exception("Forbidden")
    } else {
      return messages.flux
    }
  }
}
