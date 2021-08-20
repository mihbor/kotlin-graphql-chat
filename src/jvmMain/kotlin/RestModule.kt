
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import model.Credentials
import model.Token
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

const val jwtSecret = "Zn8Q5tyZ/G1MHltc4F/gTkVJMlrbKiZt"

@RestController("/")
open class RestModule {
  @PostMapping("/login")
  open fun login(@RequestBody credentials: Credentials): ResponseEntity<Any> {

    val user = data.users.find { it.id == credentials.id }
    if (user == null || user.password != credentials.password) {
      return ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED)
    } else {
      val token = JWT.create()
        .withSubject(user.id)
        .sign(Algorithm.HMAC256(jwtSecret))
      return ResponseEntity(Token(token), HttpStatus.OK)
    }
  }
}
