package auth

import API
import externals.decodeJWT
import kotlinx.browser.localStorage
import model.Credentials

const val accessTokenKey = "accessToken"
const val loginUrl = "http://localhost:9000/login"

fun getUserFromToken(token: String): String = decodeJWT(token).sub

val accessToken get() = localStorage.getItem(accessTokenKey)

fun getLoggedInUser() = accessToken?.let (::getUserFromToken)

fun isLoggedIn() = !localStorage.getItem(accessTokenKey).isNullOrEmpty()

suspend fun logIn(email: String, password: String) =
  API.login(Credentials(email, password))
    .also{ localStorage.setItem(accessTokenKey, it.token) }
    .let{ getUserFromToken(it.token) }

fun logOut() = localStorage.removeItem(accessTokenKey)
