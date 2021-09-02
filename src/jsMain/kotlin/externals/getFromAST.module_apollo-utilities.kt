@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("apollo-utilities")
@file:JsNonModule

package apollo

import graphql.DocumentNode
import graphql.FragmentDefinitionNode
import graphql.OperationDefinitionNode

external fun getMutationDefinition(doc: DocumentNode): OperationDefinitionNode

external fun checkDocument(doc: DocumentNode): DocumentNode

external fun getOperationDefinition(doc: DocumentNode): OperationDefinitionNode?

external fun getOperationDefinitionOrDie(document: DocumentNode): OperationDefinitionNode

external fun getOperationName(doc: DocumentNode): String?

external fun getFragmentDefinitions(doc: DocumentNode): Array<FragmentDefinitionNode>

external fun getQueryDefinition(doc: DocumentNode): OperationDefinitionNode

external fun getFragmentDefinition(doc: DocumentNode): FragmentDefinitionNode

external fun getMainDefinition(queryDoc: DocumentNode): dynamic /* OperationDefinitionNode | FragmentDefinitionNode */

external interface FragmentMap {
    @nativeGetter
    operator fun get(fragmentName: String): FragmentDefinitionNode?
    @nativeSetter
    operator fun set(fragmentName: String, value: FragmentDefinitionNode)
}

external fun createFragmentMap(fragments: Array<FragmentDefinitionNode> = definedExternally): FragmentMap

external interface `T$36` {
    @nativeGetter
    operator fun get(key: String): JsonValue?
    @nativeSetter
    operator fun set(key: String, value: JsonValue)
}

external fun getDefaultValues(definition: OperationDefinitionNode?): `T$36`

external fun variablesInOperation(operation: OperationDefinitionNode): Set<String>