package com.cannonades.petconnect.common.data.api.utils

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.cannonades.petconnect.common.data.api.ApiConstants
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream

class FakeServer {
    private val mockWebServer = MockWebServer()

    private val endpointSeparator = "/"
    private val responsesBasePath = "networkresponses/"
    private val animalsEndpointPath = endpointSeparator + ApiConstants.ANIMALS_ENDPOINT
    private val notFoundResponse = MockResponse().setResponseCode(404)

    val baseEndpoint
        get() = mockWebServer.url(endpointSeparator)

    fun start() {
        mockWebServer.start(8080)
    }

    fun setHappyPathDispatcher() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return notFoundResponse

                return with(path) {
                    when {
                        startsWith(animalsEndpointPath) -> {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(getJson("${responsesBasePath}animals.json"))
                        }
                        else -> {
                            notFoundResponse
                        }
                    }
                }
            }
        }
    }

    fun shutdown() {
        mockWebServer.shutdown()
    }

    private fun getJson(path: String): String {
        return try {
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open(path)
            String(jsonStream.readBytes())
        } catch (exception: IOException) {
//      Logger.e(exception, "Error reading network response json asset")
            Log.e("FakeServer", "Error reading network response json asset: $exception")
            throw exception
        }
    }
}