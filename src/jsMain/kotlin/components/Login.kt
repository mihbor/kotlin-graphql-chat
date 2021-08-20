package components

import auth.logIn
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RProps
import react.dom.*
import react.functionalComponent
import react.useState

external interface LoginFormProps: RProps {
  var onLogin: (String) -> Unit
}

val login = functionalComponent<LoginFormProps> { props ->
  val (name, setName) = useState("")
  val (password, setPassword) = useState("")
  val (error, setError) = useState(false)

  val onSubmit: (Event) -> Unit = { event ->
    event.preventDefault()
    scope.launch {
      try {
        logIn(name, password).let {
          props.onLogin(name)
        }
      } catch (e: Exception) {
        setError(true)
      }
    }
  }

  form {
    attrs.onSubmitFunction = onSubmit
    div("field") {
      label("label") { +"Username" }
      div("control") {
        input(InputType.text, classes = "input") {
          attrs {
            this.name = "username"
            value = name
            onChangeFunction = { setName((it.target as HTMLInputElement).value) }
          }
        }
      }
    }
    div("field") {
      label("label") { +"Password" }
      input(InputType.password, classes = "input") {
        attrs {
          this.name = "password"
          value = password
          onChangeFunction = { setPassword((it.target as HTMLInputElement).value) }
        }
      }
    }
    div("field") {
      p("help is-danger") { if(error) +"Invalid credentials" }
      div("control") {
        button(classes = "button is-link", type = ButtonType.submit) {
          +"Login"
        }
      }
    }
  }
}