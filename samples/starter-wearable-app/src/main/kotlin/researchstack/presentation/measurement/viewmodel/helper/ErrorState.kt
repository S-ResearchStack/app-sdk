package researchstack.presentation.measurement.viewmodel.helper

enum class ErrorState {
    Normal {
        override fun nextState() = FirstError
    },
    FirstError {
        override fun nextState() = MoreError
    },
    MoreError {
        override fun nextState() = MoreError
    }, ;

    abstract fun nextState(): ErrorState
    fun resetState() = Normal

    fun isFirstError() = this == FirstError

    fun isNormal() = this == Normal
}
