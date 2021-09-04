@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("@apollo/react-hooks")
@file:JsNonModule

package apollo.react

import graphql.DocumentNode

external fun <TData, TVariables> useMutation(mutation: DocumentNode, options: MutationHookOptions<TData, TVariables> = definedExternally): dynamic /* JsTuple<(options: MutationFunctionOptions<TData, TVariables>) -> Promise<ExecutionResult<TData>>, MutationResult<TData>> */