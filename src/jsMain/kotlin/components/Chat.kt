package components

import chat.useChat
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.section
import react.functionalComponent

external interface ChatProps: RProps {
  var user: String?
}

val chat = functionalComponent<ChatProps> { props ->
  val (messages, addMessage) = useChat()
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
        attrs.onSend = addMessage
      }
    }
  }
}