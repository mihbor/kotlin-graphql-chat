import model.Message
import model.User

object data {
  val messages = mutableListOf(
    Message(
      id = "SJKhIAKYB",
      from = "system",
      text = "Welcome!"
    )
  )

  val users = listOf(
    User(
      id = "alice",
      password = "alice123",
    ),
    User(
      id = "bob",
      password = "bob123",
    ),
    User(
      id = "charlie",
      password = "charlie123",
    )
  )
  val usersById = users.map{ it.id to it }.toMap()

}