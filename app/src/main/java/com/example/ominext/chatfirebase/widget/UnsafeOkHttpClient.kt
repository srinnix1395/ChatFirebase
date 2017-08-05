package com.example.ominext.chatfirebase.widget


/**
 * Created by anhtu on 8/5/2017.
 */

object UnsafeOkHttpClient {

//    val unsafeOkHttpClient: OkHttpClient
//        get() {
//            try {
//                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager() {
//                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
//
//                    }
//
//                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
//
//                    }
//
//                    override fun getAcceptedIssuers(): Array<X509Certificate> {
//
//                    }
//
//                })
//                val sslContext = SSLContext.getInstance("SSL")
//                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
//                val sslSocketFactory = sslContext.getSocketFactory()
//
//                val okHttpClient = OkHttpClient()
//                okHttpClient.setSslSocketFactory(sslSocketFactory)
//                okHttpClient.setProtocols(Arrays.asList(Protocol.HTTP_1_1))
//                okHttpClient.setHostnameVerifier(object : HostnameVerifier() {
//                    fun verify(hostname: String, session: SSLSession): Boolean {
//                        return true
//                    }
//                })
//
//                return okHttpClient
//            } catch (e: Exception) {
//                throw RuntimeException(e)
//            }
//
//        }
}