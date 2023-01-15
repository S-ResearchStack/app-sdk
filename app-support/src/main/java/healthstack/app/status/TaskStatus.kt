package healthstack.app.status

import healthstack.app.R

object TaskStatus : StatusDataType() {
    override fun getIcon(): Int = R.drawable.ic_task

    // FIXME currently, not used
    override suspend fun getLatestStatus(): Any? = null

    override fun getUnitString(): String = "Tasks\nRemaining"
}
