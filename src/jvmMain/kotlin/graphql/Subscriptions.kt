package graphql

import com.expediagroup.graphql.server.operations.Subscription
import model.Message
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*

@Component
class Subscriptions : Subscription {
  fun messages(): Flux<Message> = Flux.interval(Duration.ofSeconds(1)).take(5).map {
    Message(UUID.randomUUID().toString(), "some text $it", "from $it")
  }
}
