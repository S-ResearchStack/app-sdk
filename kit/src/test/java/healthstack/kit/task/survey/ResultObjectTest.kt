package healthstack.kit.task.survey

import healthstack.kit.task.survey.question.component.ResultObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Result Object Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResultObjectTest {
    @Tag("positive")
    @Test
    fun `update start time test`() {
        // when
        var resultObject = ResultObject()

        // given
        resultObject = resultObject.updateEndDate("end-date")
        resultObject = resultObject.updateEndTime("end-time")
        resultObject = resultObject.updateStartTime("start-time")
        resultObject = resultObject.updateStartDate("start-date")

        // then
        Assertions.assertEquals(resultObject.endTime, "end-time")
        Assertions.assertEquals(resultObject.endDate, "end-date")
        Assertions.assertEquals(resultObject.startTime, "start-time")
        Assertions.assertEquals(resultObject.startDate, "start-date")
    }
}
