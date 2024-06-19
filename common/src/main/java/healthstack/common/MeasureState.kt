package healthstack.common

enum class MeasureState {
    Initial,
    Measuring,
    Motion,
    MotionOverLoad,
    Completed,
    Failed,
}
