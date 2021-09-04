package components

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
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.section
import react.functionalComponent
import react.useState
import useMutation

external interface ChatProps: RProps {
  var user: String?
}

val chat = functionalComponent<ChatProps> { props ->
  val (messages, setMessages) = useState(emptyList<Message>())
  val result = useQuery(messagesQuery, (js("{}") as QueryHookOptions<dynamic, dynamic>).apply {
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
  fun handleSend(text: String) {
    addMessage(MutationFunctionOptions{this.text = text})
  }
  section("section") {
    if (result.loading) +"Loading..."
    else if (result.error != null) +"ERROR!"
    else div("container") {
      h1("title") {
        +"Chatting as user ${props.user}"
      }
      child(messageList) {
        attrs.user = props.user
        attrs.messages = messages
      }
      child(messageInput) {
        attrs.onSend = ::handleSend
      }
    }
  }
}