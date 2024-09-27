package researchstack.wearable.standalone.presentation.measurement.viewmodel.helper

fun Float.lbsToKg(isMetric: Boolean): Float = if (isMetric) {
    this
} else this * LBS_TO_KG_RATIO

fun Float.kgToLbs(isMetric: Boolean): Float = if (isMetric) {
    this
} else this / LBS_TO_KG_RATIO

fun Float.ftToCm(isMetric: Boolean): Float = if (isMetric) {
    this
} else this * FT_TO_CM_RATIO

fun Float.cmToFt(isMetric: Boolean): Float = if (isMetric) {
    this
} else this / FT_TO_CM_RATIO

private const val LBS_TO_KG_RATIO = 0.45359236f
private const val FT_TO_CM_RATIO = 30.48f
