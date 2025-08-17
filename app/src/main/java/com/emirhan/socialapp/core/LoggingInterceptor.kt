package com.emirhan.socialapp.core

import io.grpc.okhttp.internal.Platform.logger
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.String
import java.util.Locale

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val t1 = System.nanoTime()
        logger.info(
            String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
        )

        val response: Response = chain.proceed(request)

        val t2 = System.nanoTime()
        logger.info(
            String.format(Locale.getDefault(),
                "Received response for %s in %.1fms%n%s",
                response.request.url, (t2 - t1) / 1e6, response.headers)
        )

        return response
    }
}