@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("apollo-link-ws")
@file:JsNonModule

package apollo

import ZenObservable.Observable

external open class WebSocketLink : ApolloLink {
    constructor(paramsOrClient: Configuration)
    open var subscriptionClient: Any
    open fun request(operation: Operation): Observable<LocalExecutionResult<dynamic> /* ExecutionResult<dynamic> & `T$14`<Record<string, any>, Record<string, any>> */>?
    interface Configuration {
        var uri: String
        var options: ClientOptions?
            get() = definedExternally
            set(value) = definedExternally
        var webSocketImpl: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
}
