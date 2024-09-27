package researchstack.domain.model.shealth

enum class SHealthDataType : HealthTypeEnum {
    // Samsung Health Data
    BLOOD_GLUCOSE,
    BLOOD_PRESSURE,
    EXERCISE,
    HEART_RATE,
    HEIGHT,
    OXYGEN_SATURATION,
    SLEEP_SESSION,
    SLEEP_STAGE,
    STEPS,
    WEIGHT,

    // etc
    RESPIRATORY_RATE,
    TOTAL_CALORIES_BURNED,
    UNSPECIFIED,
}
