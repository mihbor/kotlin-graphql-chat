@file:JsModule("jsonwebtoken")
@file:JsNonModule

package externals

@JsName("decode")
external fun decodeJWT(jwt: String, options: dynamic = definedExternally): dynamic
