//package com.prodmobile.template
//
//import com.prodmobile.template.data.DefaultTemplateApi
//import io.ktor.client.HttpClient
//import io.ktor.client.request.get
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.appendPathSegments
//import kotlinx.coroutines.runBlocking
//import org.junit.Test
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//class ExampleUnitTest {
//    @Test
//    fun pingResponds() {
//        val client = HttpClient()
//        val endpoint = DefaultTemplateApi()
//        val request = runBlocking {
//            client.get(endpoint.url.appendPathSegments("ping").build())
//        }
//        assert(request.status == HttpStatusCode.OK)
//    }
//}