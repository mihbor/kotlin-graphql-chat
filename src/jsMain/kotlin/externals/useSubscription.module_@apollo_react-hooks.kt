@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("@apollo/react-hooks")
@file:JsNonModule

package apollo.react

import graphql.DocumentNode

external interface SubscriptionHookResult<TVariables, TData> {
    var variables: TVariables?
    var loading: Boolean
    var data: TData?
        get() = definedExternally
        set(value) = definedExternally
    var error: Any?
        get() = definedExternally
        set(value) = definedExternally
}

external fun <TData, TVariables> useSubscription(subscription: DocumentNode, options: SubscriptionHookOptions<TData, TVariables> = definedExternally): SubscriptionHookResult<TVariables, TData>