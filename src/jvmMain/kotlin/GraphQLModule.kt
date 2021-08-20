/*
 * Copyright 2021 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.getGraphQLServer
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Application.graphQLModule() {
    install(Routing)
    install(WebSockets)
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "myRealm"
            verifier(
                JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .build()
            )
            validate { credential ->
                credential.payload
                    .takeIf { it.subject != "" }
                    ?.let (::JWTPrincipal)
            }
        }
    }
    routing {
        authenticate("auth-jwt", optional = true) {
            post("graphql") {
                handle(call)
            }
        }
        webSocket("/subscriptions") {
            println("subscriptions websocket!")
            try {
                for (frame in incoming) {
                    if(frame is Frame.Text) {
                        val receivedText = frame.readText()
                        println(receivedText)
                    }
                }
                val response = """{"type":"connection_ack"}"""
                send(response)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }

        get("playground") {
            call.respondText(buildPlaygroundHtml("graphql", "subscriptions"), ContentType.Text.Html)
        }
    }
}

val mapper = jacksonObjectMapper()
val ktorGraphQLServer = getGraphQLServer(mapper)

/**
 * Handle incoming Ktor Http requests and send them back to the response methods.
 */
suspend fun handle(applicationCall: ApplicationCall) {
    // Execute the query against the schema
    val auth = applicationCall.request.header("Authorization")
    val result = ktorGraphQLServer.execute(applicationCall.request)

    if (result != null) {
        // write response as json
        val json = mapper.writeValueAsString(result)
        applicationCall.response.call.respond(json)
    } else {
        applicationCall.response.call.respond(HttpStatusCode.BadRequest, "Invalid request")
    }
}
private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionsEndpoint: String) =
    Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
        ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
        ?.replace("\${subscriptionsEndpoint}", subscriptionsEndpoint)
        ?: throw IllegalStateException("graphql-playground.html cannot be found in the classpath")
