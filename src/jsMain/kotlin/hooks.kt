package chat

import API.addMessageMutation
import API.messageAddedSubscription
import API.messagesQuery
import MutationFunctionOptions
import apollo.react.QueryHookOptions
import apollo.react.SubscriptionHookOptions
import apollo.react.useQuery
import apollo.react.useSubscription
import json
import kotlinx.serialization.json.decodeFromDynamic
import model.Message
import react.useState
import useMutation

fun useChat(): Pair<List<Message>, (String) -> Unit> {
  val (messages, setMessages) = useState(emptyList<Message>())
  useQuery(messagesQuery, (js("{}") as QueryHookOptions<dynamic, dynamic>).apply {
    this.onCompleted = {
      setMessages(json.decodeFromDynamic(it.messages))
    }
  })
  useSubscription(messageAddedSubscription, (js("{}") as SubscriptionHookOptions<dynamic, dynamic>).apply {
    this.onSubscriptionData = {
      setMessages(messages + json.decodeFromDynamic<Message>(it.subscriptionData.data.messageAdded))
    }
  })
  val (addMessage, _) = useMutation(addMessageMutation)
  return Pair(messages) { text -> addMessage(MutationFunctionOptions { this.text = text }) }
}