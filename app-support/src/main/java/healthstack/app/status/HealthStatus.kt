package healthstack.app.status

import healthstack.app.viewmodel.HealthStatusViewModel

abstract class HealthStatus : StatusDataType() {
    abstract fun toViewModel(): HealthStatusViewModel
}
