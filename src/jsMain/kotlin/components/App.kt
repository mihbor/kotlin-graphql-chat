package components

import kotlinx.coroutines.MainScope
import react.RProps
import react.child
import react.functionalComponent
import react.useState

val scope = MainScope()

val app = functionalComponent<RProps>{

  val (user, setUser) = useState(auth.getLoggedInUser())
  val logIn: (String) -> Unit = { setUser(it) }
  val logOut: () -> Unit = { setUser(null) }

  if (user == null) {
    child(login) {
      attrs.onLogin = logIn
    }
  } else {
    child(navBar) {
      attrs.onLogout = logOut
    }
    child(chat) {
      attrs.user = user
    }
  }
}
