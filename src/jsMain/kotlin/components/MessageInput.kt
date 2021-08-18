package components

import json
import kotlinx.html.InputType
import kotlinx.html.js.onKeyPressFunction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.encodeToDynamic
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.RProps
import react.dom.div
import react.dom.input
import react.dom.p
import react.functionalComponent

external interface MessageInputProps: RProps {
  var onSend: (String) -> Unit
}

val messageInput = functionalComponent<MessageInputProps> { props ->
  val handleKeyPress = { event: dynamic ->
    if (event.type == "keypress" && event.key == "Enter") {
      val target: dynamic = event.target
      props.onSend(target.value)
      target.value = ""
    }
  }
  div("box") {
    p("control") {
      input(InputType.text, classes = "input") {
        attrs.onKeyPressFunction = handleKeyPress
      }
    }
  }
}