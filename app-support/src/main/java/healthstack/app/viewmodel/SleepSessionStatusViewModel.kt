package healthstack.app.viewmodel

import healthstack.app.status.SleepSessionStatus

object SleepSessionStatusViewModel : HealthStatusViewModel(
    SleepSessionStatus, 60000L
)
