package components

import API.addMessage
import API.getMessages
import API.onMessageAdded
import kotlinx.coroutines.launch
import model.Message
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.section
import react.functionalComponent
import react.useEffect
import useStateUpdater

external interface ChatProps: RProps {
  var user: String?
}

val chat = functionalComponent<ChatProps> { props ->
  val (messages, updateMessages) = useStateUpdater(listOf<Message>())
  fun appendMessage(message: Message) {
    console.log("message added: $message")
    updateMessages{ it + message }
  }
  useEffect(emptyList()) {
    scope.launch {
      val msgs = getMessages()
      updateMessages{ msgs }
    }
    onMessageAdded{
      appendMessage(it)
    }
  }
  fun handleSend(text: String) {
    scope.launch {
      addMessage(text)
    }
  }
  section("section") {
    div("container") {
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