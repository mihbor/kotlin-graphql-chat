import graphql.gql
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import model.Credentials
import model.Message
import model.Token

val origin = window.location.origin // only needed until https://youtrack.jetbrains.com/issue/KTOR-453 is resolved
val json = Json {
  isLenient = true
  ignoreUnknownKeys = true
  allowSpecialFloatingPointValues = true
  useArrayPolymorphism = true
  allowStructuredMapKeys = true
  encodeDefaults = false
}
val http = HttpClient {
  install(JsonFeature) {
    serializer = KotlinxSerializer(json)
    acceptContentTypes += ContentType.Text.Plain

  }
}

object API {
  suspend fun login(credentials: Credentials) = http.post<Token>("$origin/login") {
    contentType(ContentType.Application.Json)
    body = credentials
  }

  val messagesQuery = gql("""
    query MessagesQuery {
      messages { id from text }
    }""")

  val addMessageMutation = gql("mutation AddMessageMutation(\$text: String!) {" +
    "  message: addMessage(text: \$text) { id from text }" +
    "}")

  val messageAddedSubscription = gql("""
    subscription subscription {
      messageAdded { id text from }
    }""")

  fun onMessageAdded(handleMessage: (Message) -> Unit): () -> Unit {
    val observable = Apollo.subscribe(messageAddedSubscription)
    val subscription = observable.subscribe {
      console.log("Result: ${JSON.stringify(it.data)}")
      handleMessage(json.decodeFromDynamic(it.data.messageAdded))
    }
    return { subscription.unsubscribe() }
  }

  suspend fun addMessage(text: String) = json.decodeFromDynamic<Message>(
    Apollo.mutate(addMessageMutation) { this.text = text }
      .data.message
  )

  suspend fun getMessages() = json.decodeFromDynamic<List<Message>>(
    Apollo.query(messagesQuery)
      .data.messages
  )
}
