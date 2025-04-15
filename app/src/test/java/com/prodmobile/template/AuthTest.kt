//package com.prodmobile.template
//
//import com.prodmobile.template.core.http_client.HttpClientFactory
//import com.prodmobile.template.core.result.onError
//import com.prodmobile.template.core.result.onSuccess
//import com.prodmobile.template.data.DefaultTemplateApi
//import com.prodmobile.template.data.auth.mentor.model.SignInData
//import com.prodmobile.template.data.auth.mentor.model.SignUpData
//import com.prodmobile.template.data.auth.student.remote.StudentAuthRemoteDataSourceImpl
//import kotlinx.coroutines.runBlocking
//import org.junit.Test
//
//class AuthTest {
//    @Test
//    fun signIn() {
//        runBlocking {
//            val result = StudentAuthRemoteDataSourceImpl(
//                HttpClientFactory.create(),
//                DefaultTemplateApi()
//            ).signIn(SignInData("test_full_name"))
//            result.onSuccess {
//                println("Sign in successful: $it")
//            }
//            result.onError {
//                println("Sign in failed: $it, ${it.message}")
//            }
//        }
//    }
//
//    @Test
//    fun signUp() {
//        runBlocking {
//            val result = StudentAuthRemoteDataSourceImpl(
//                HttpClientFactory.create(),
//                DefaultTemplateApi()
//            ).signUp(SignUpData("test_full_name", 69, listOf("kotlin")))
//            result.onSuccess {
//                println("Sign in successful: $it")
//            }
//            result.onError {
//                println("Sign in failed: $it, ${it.message}")
//            }
//        }
//    }
//}
