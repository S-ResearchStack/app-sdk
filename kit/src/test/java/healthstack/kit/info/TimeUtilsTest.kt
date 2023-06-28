package healthstack.kit.info

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("AppColors Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeUtilsTest {
    @Tag("positive")
    @Test
    fun `time test`() {
        // morning
        var hour = 3
        var minute = 30

        var result = TimeUtils.formatTime(hour, minute)

        assertEquals(result, "03:30 AM")

        // noon
        hour = 12
        minute = 30

        result = TimeUtils.formatTime(hour, minute)

        assertEquals(result, "12:30 PM")

        // midnight
        hour = 0
        minute = 30

        result = TimeUtils.formatTime(hour, minute)

        assertEquals(result, "12:30 AM")

        // evening
        hour = 15
        minute = 30

        result = TimeUtils.formatTime(hour, minute)

        assertEquals(result, "03:30 PM")
    }
}
