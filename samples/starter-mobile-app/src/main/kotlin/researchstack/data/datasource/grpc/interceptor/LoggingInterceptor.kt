package researchstack.data.datasource.grpc.interceptor

import android.util.Log
import io.grpc.CallOptions
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor

private const val TAG = "LoggingInterceptor"

class LoggingInterceptor : ClientInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: io.grpc.Channel,
    ): ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> =
        object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
            next.newCall(method, callOptions)
        ) {

            override fun sendMessage(message: ReqT) {
                Log.i(TAG, "[${method.bareMethodName}] send message")
                super.sendMessage(message)
            }

            @Suppress("TooGenericExceptionCaught")
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                super.start(
                    object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                        responseListener
                    ) {
                        override fun onMessage(message: RespT) {
                            Log.i(TAG, "[${method.bareMethodName}] response message $message")
                            super.onMessage(message)
                        }
                    },
                    headers
                )
            }
        }
}
