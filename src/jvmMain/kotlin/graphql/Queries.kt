package graphql

import com.expediagroup.graphql.server.operations.Query
import data
import model.Message

class Queries : Query {
  fun messages(context: AuthContext): List<Message> =
    data.messages
}
