package chat

import model.Message
import java.util.UUID
import java.time.Duration
import reactor.core.publisher.Flux
import org.springframework.stereotype.Component

@Component
class Messages {
  lateinit var emit: (Message) -> Unit
  val flux = Flux.create<Message>{ emitter ->
    emit = { emitter.next(it) }
  }.share()
}