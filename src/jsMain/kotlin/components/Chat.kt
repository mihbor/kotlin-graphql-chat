package components

import API.addMessage
import API.getMessages
import kotlinx.coroutines.launch
import model.Message
import react.*
import react.dom.div
import react.dom.h1
import react.dom.section

external interface ChatProps: RProps {
  var user: String?
}
val chat = functionalComponent<ChatProps> { props ->
  val (messages, setMessages) = useState(listOf<Message>())
  useEffect(emptyList()) {
    scope.launch {
      setMessages(getMessages())
    }
  }
  fun handleSend(text: String) {
    scope.launch {
      val message = addMessage(text)
      setMessages(messages + message)
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