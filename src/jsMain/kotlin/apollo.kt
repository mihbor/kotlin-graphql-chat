import apollo.*
import apollo.ApolloLink.Companion.split
import apollo.DataProxy.WriteQueryOptions
import auth.accessToken
import auth.isLoggedIn
import graphql.DocumentNode
import graphql.OperationDefinitionNode
import graphql.gql
import kotlinx.coroutines.await

val authLink = ApolloLink { operation, forward ->
  if (isLoggedIn()) {
    val headers = js("{}")
    headers.Authorization = "Bearer $accessToken"
    val context = js("{}")
    context.headers = headers
    operation.setContext(context)
  }
  forward(operation)
}

val httpLink = ApolloLink.from(arrayOf(
  authLink,
  HttpLink((js("{}") as HttpLink.Options).apply {
    uri = "http://localhost:8080/graphql"
  })
))

val wsLink = WebSocketLink((js("{}") as WebSocketLink.Configuration).apply {
  uri = "ws://localhost:8080/subscriptions"
  options = ClientOptions(lazy = true, reconnect = true) {
    Authorization = "Bearer $accessToken"
  }
})

val apolloCache = InMemoryCache()

val isSubscription: (Operation) -> Boolean = {
  val definition = getMainDefinition(it.query as DocumentNode) as OperationDefinitionNode
  definition.kind == "OperationDefinition" && definition.operation == "subscription"
}

val apolloClient = ApolloClient(
  (js("{}") as ApolloClientOptions<NormalizedCacheObject>).apply {
    link = split(isSubscription, wsLink, httpLink)
    cache = apolloCache
  }
)

fun <TVariables> QueryOptions(
  query: DocumentNode,
  fetchPolicy: String?,
  variablesProvider: (dynamic.() -> Unit)?
) = (js("{}") as QueryOptions<TVariables>).apply {
  this.query = query
  this.fetchPolicy = fetchPolicy
  variablesProvider?.let {
    this.variables = provide(it)
  }
}

fun <T, TVariables> MutationOptions(
  mutation: DocumentNode,
  fetchPolicy: String?,
  update: MutationUpdaterFn<T>?,
  variablesProvider: (dynamic.() -> Unit)?
) = (js("{}") as MutationOptions<T, TVariables>).apply {
  this.mutation = mutation
  this.fetchPolicy = fetchPolicy
  this.update = update
  variablesProvider?.let {
    this.variables = provide(it)
  }
}

fun <TVariables> SubscriptionOptions(
  subscription: DocumentNode,
  variablesProvider: (dynamic.() -> Unit)?
) = (js("{}") as SubscriptionOptions<TVariables>).apply {
  this.query = subscription
  variablesProvider?.let {
    this.variables = provide(it)
  }
}

fun ClientOptions(
  lazy: Boolean?,
  reconnect: Boolean?,
  connectionParams: (dynamic.() -> Unit)?
) = (js("{}") as ClientOptions).apply {
  this.lazy = lazy
  this.reconnect = reconnect
  connectionParams?.let {
    this.connectionParams = { provide(it) }
  }
}

fun WriteQueryOptions(
  query: DocumentNode,
  data: dynamic,
  variablesProvider: (dynamic.() -> Unit)?
) = (js("{}") as WriteQueryOptions<dynamic, Any?>).apply {

  this.query = query
  this.data = data
  variablesProvider?.let {
    this.variables = provide(it)
  }
}

fun provide(provider: (dynamic.() -> Unit)) : dynamic {
  val container = js("{}")
  provider.invoke(container)
  return container
}

object Apollo {
  suspend fun query(
    query: String,
    fetchPolicy: String? = null,
    variables: (dynamic.() -> Unit)? = null
  ) = query(gql(query), fetchPolicy, variables)

  suspend fun query(
    query: DocumentNode,
    fetchPolicy: String? = null,
    variables: (dynamic.() -> Unit)? = null
  ) = apolloClient.query<dynamic, Any?>(QueryOptions(query, fetchPolicy, variables)).await()

  suspend fun mutate(
    mutation: String,
    fetchPolicy: String? = null,
    update: MutationUpdaterFn<dynamic>? = null,
    variables: (dynamic.() -> Unit)? = null
  ) = mutate(gql(mutation), fetchPolicy, update, variables)

  suspend fun mutate(
    mutation: DocumentNode,
    fetchPolicy: String? = null,
    update: MutationUpdaterFn<dynamic>? = null,
    variables: (dynamic.() -> Unit)? = null
  ) = apolloClient.mutate<dynamic, Any?>(MutationOptions(mutation, fetchPolicy, update, variables)).await()

  fun subscribe(
    subscription: DocumentNode,
    variables: (dynamic.() -> Unit)? = null
  ) = apolloClient.subscribe<dynamic, Any?>(SubscriptionOptions(subscription, variables))
}