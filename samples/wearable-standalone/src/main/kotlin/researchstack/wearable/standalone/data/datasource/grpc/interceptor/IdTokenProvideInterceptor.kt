package researchstack.wearable.standalone.data.datasource.grpc.interceptor

import io.grpc.CallOptions
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import kotlinx.coroutines.runBlocking
import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import javax.inject.Inject

class IdTokenProvideInterceptor @Inject constructor(
    private val authRepositoryWrapper: AuthRepositoryWrapper,
) : ClientInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: io.grpc.Channel,
    ) = object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        next.newCall(method, callOptions)
    ) {
        override fun start(responseListener: Listener<RespT>, headers: Metadata) {
            runBlocking {
                authRepositoryWrapper.getIdToken()
                    .recoverCatching { authRepositoryWrapper.getIdToken().getOrThrow() }
                    .onSuccess { (token, issuer) ->
                        headers.put(AUTHORIZATION, "Bearer $token")
                        headers.put(JWT_ISSUER, issuer)
                    }
            }

            super.start(
                object :
                    ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                        responseListener
                    ) {
                    override fun onHeaders(headers: Metadata) {
                        super.onHeaders(headers)
                    }
                },
                headers
            )
        }
    }

    companion object {
        val AUTHORIZATION: Metadata.Key<String> =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
        val JWT_ISSUER: Metadata.Key<String> =
            Metadata.Key.of("jwt-issuer", Metadata.ASCII_STRING_MARSHALLER)
    }
}
