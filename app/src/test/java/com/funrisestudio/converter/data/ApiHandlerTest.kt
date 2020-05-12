package com.funrisestudio.converter.data

import com.funrisestudio.converter.data.exceptions.NetworkException
import com.funrisestudio.converter.data.source.ApiHandler
import com.funrisestudio.converter.testutils.test
import io.reactivex.Single
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import org.junit.Test
import retrofit2.Response

class ApiHandlerTest {

    private val apiHandler = ApiHandler()

    @Test
    fun `should handle successful request correctly`() {
        val body: Int? = 200
        val rawRequest = Single.just(Response.success(body))
        apiHandler.handleRequestSingle(rawRequest).test {
            assertValueCount(1)
            assertNoErrors()
            assertComplete()
            assertValue(body)
            assertTerminated()
        }
    }

    @Test
    fun `should handle failed request correctly`() {
        val errorBody = RealResponseBody("", 1, Buffer())
        val rawRequest = Single.just(Response.error<Int?>(400, errorBody))
        apiHandler.handleRequestSingle(rawRequest).test {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }
    }

    @Test
    fun `should handle null-body request correctly`() {
        val body: Int? = null
        val rawRequest = Single.just(Response.success(body))
        apiHandler.handleRequestSingle(rawRequest).test {
            assertNoValues()
            assertError(NetworkException::class.java)
            assertNotComplete()
            assertTerminated()
        }
    }

}