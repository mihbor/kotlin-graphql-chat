package model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
  val token: String
)

@Serializable
data class Credentials(
  val id: String,
  val password: String
)

@Serializable
data class User(
  val id: String,
  val password: String
)

@Serializable
data class Message(
  val id: String,
  val text: String,
  val from: String
)