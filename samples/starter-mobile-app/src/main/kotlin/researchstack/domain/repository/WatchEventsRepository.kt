package researchstack.domain.repository

import researchstack.domain.model.Timestamp

interface WatchEventsRepository<T : Timestamp> {
    fun insert(data: T)
}
