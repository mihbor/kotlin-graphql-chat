package components

import kotlinx.html.js.onClickFunction
import react.RProps
import react.dom.a
import react.dom.div
import react.dom.nav
import react.functionalComponent

external interface NavBarProps : RProps {
  var onLogout: () -> Unit
}

val navBar = functionalComponent<NavBarProps> { props ->
  nav(classes = "navbar") {
    div(classes = "navbar-end") {
      a(classes = "navbar-item") {
        attrs.onClickFunction = { event ->
          props.onLogout()
        }
        +"Log out"
      }
    }
  }
}