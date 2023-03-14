package healthstack.backend.integration.adapter

import com.google.gson.Gson
import com.google.gson.JsonArray
import healthstack.backend.integration.task.ItemResult
import healthstack.backend.integration.task.TaskResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
internal class HealthStackBackendAPITest {
    private val server = MockWebServer()
    private lateinit var backendAPI: HealthStackBackendAPI
    private val gson = Gson()

    @BeforeEach
    fun setUp() {
        server.start()
        backendAPI = RetrofitFactory.create(server.url("/").toString(), HealthStackBackendAPI::class.java)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Tag("positive")
    @Test
    fun `uploadTestResult should translate task-result to json`() {
        val projectId = "1"

        val dispatcher: Dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                if (request.path == "/api/projects/$projectId/tasks") {
                    return MockResponse().setResponseCode(200)
                }
                return MockResponse().setResponseCode(404)
            }
        }
        server.dispatcher = dispatcher
        val itemResult = ItemResult("itemName", "result")
        val taskResult = TaskResult(
            "userId", "taskId", 0,
            "2022-09-12T12:00:00", "2022-09-12T12:00:00", listOf(itemResult)
        )
        runTest {
            backendAPI.uploadTaskResult("id-token", projectId, listOf(taskResult))

            val jsonArray = gson.fromJson(
                server.takeRequest().body.readUtf8(),
                JsonArray::class.java
            )
            assertEquals(1, jsonArray.size())
            val taskResultJson = jsonArray[0].asJsonObject
            assertEquals(taskResult.userId, taskResultJson["userId"].asString)
            assertEquals(taskResult.taskId, taskResultJson["taskId"].asString)
            assertEquals(taskResult.revisionId, taskResultJson["revisionId"].asInt)
            assertEquals(taskResult.startedAt, taskResultJson["startedAt"].asString)
            assertEquals(taskResult.submittedAt, taskResultJson["submittedAt"].asString)

            val itemResultArray = taskResultJson["itemResults"].asJsonArray
            assertEquals(1, itemResultArray.size())
            val itemResultJson = itemResultArray[0].asJsonObject
            assertEquals(itemResult.itemName, itemResultJson["itemName"].asString)
            assertEquals(itemResult.result, itemResultJson["result"].asString)
        }
    }
}
