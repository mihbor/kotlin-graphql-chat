package components

import kotlinx.css.*
import model.Message
import react.RBuilder
import react.RProps
import react.dom.*
import styled.styledDiv as div
import styled.styledTd as td
import react.functionalComponent
import react.useRef
import styled.css

external interface MessageListProps: RProps {
  var user: String?
  var messages: List<Message>?
}

fun RBuilder.renderMessage(message: Message, isPrimary: Boolean) = tr {
  attrs.key = message.id
  td {
    span(if(isPrimary) "tag is-primary" else "tag") {
      +message.from
    }
  }
  td {
    css {
      paddingLeft = LinearDimension("0.75em")
    }
    +message.text
  }
}

val messageList = functionalComponent<MessageListProps> { props ->
  val boxRef = useRef(null)
  div {
    css {
      height = LinearDimension("50vh")
      overflowY = Overflow.scroll
    }
    ref = boxRef
    table {
      tbody {
        props.messages?.map { renderMessage(it, props.user == it.from) }
      }
    }
  }
}