package healthstack.app.task.spec

import healthstack.app.task.entity.Task.Properties
import healthstack.backend.integration.task.TaskSpec
import org.quartz.CronExpression
import java.text.ParseException
import java.time.LocalDateTime
import java.util.Date

object TaskGenerator {

    @Throws(ParseException::class)
    fun generate(spec: TaskSpec): List<healthstack.app.task.entity.Task> {
        val startTime: Date = TimeUtil.stringToDate(spec.startTime)
        val endTime: Date = TimeUtil.stringToDate(spec.endTime)
        val cronExpression = CronExpression(spec.schedule)

        val schedules: MutableList<Date> =
            if (cronExpression.isSatisfiedBy(startTime))
                mutableListOf(startTime)
            else
                mutableListOf()

        var currentTime: Date? = cronExpression.getTimeAfter(startTime)

        while (currentTime != null && currentTime <= endTime) {
            schedules.add(currentTime)
            currentTime = cronExpression.getTimeAfter(currentTime)
        }

        val entities = schedules.map {
            healthstack.app.task.entity.Task(
                null,
                spec.revisionId,
                spec.taskId,
                spec.type,
                Properties(spec.title, spec.description, spec.items),
                null,
                LocalDateTime.now(),
                TimeUtil.dateToLocalDateTimeSystem(it),
                TimeUtil.dateToLocalDateTimeSystem(it).plusMinutes(spec.validTime),
                null
            )
        }
        return entities
    }
}
