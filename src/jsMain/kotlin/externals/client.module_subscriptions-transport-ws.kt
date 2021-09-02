@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("subscriptions-transport-ws")
@file:JsNonModule

package apollo

external interface ConnectionParams {
    @nativeGetter
    operator fun get(paramName: String): Any?
    @nativeSetter
    operator fun set(paramName: String, value: Any)
}

external interface ClientOptions {
    var connectionParams: dynamic /* ConnectionParams? | Function<*>? | Promise<ConnectionParams>? */
        get() = definedExternally
        set(value) = definedExternally
    var minTimeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var timeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var reconnect: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var reconnectionAttempts: Number?
        get() = definedExternally
        set(value) = definedExternally
    var connectionCallback: ((error: Array<Error>, result: Any) -> Unit)?
        get() = definedExternally
        set(value) = definedExternally
    var lazy: Boolean?
        get() = definedExternally
        set(value) = definedExternally
    var inactivityTimeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var wsOptionArguments: Array<Any>?
        get() = definedExternally
        set(value) = definedExternally
}
