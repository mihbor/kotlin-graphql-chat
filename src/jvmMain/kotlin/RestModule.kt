
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.Credentials
import model.Token

const val jwtSecret = "Zn8Q5tyZ/G1MHltc4F/gTkVJMlrbKiZt"

fun Route.login() {
  post("/login") {
    val credentials = call.receive<Credentials>()
    val user = data.users.find { it.id == credentials.id }
    if (user == null || user.password != credentials.password) {
      call.response.status(HttpStatusCode.Unauthorized)
    } else {
      val token = JWT.create()
        .withSubject(user.id)
        .sign(Algorithm.HMAC256(jwtSecret))
      call.respond(Token(token))
    }
  }
}

fun Application.restModule() {
  routing {
    login()
    static("/static") {
      resources()
    }
    static {
      resource("/js.js")
      resource("/style.css")
      resource("/{...}", "index.html")
    }
  }
}