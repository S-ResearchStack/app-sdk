package healthstack.app.viewmodel

import healthstack.app.status.HeartRateStatus

object HeartRateStatusViewModel : HealthStatusViewModel(
    HeartRateStatus, 60000L
)
