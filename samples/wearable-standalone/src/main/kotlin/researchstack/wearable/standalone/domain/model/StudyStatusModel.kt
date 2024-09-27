package researchstack.wearable.standalone.domain.model

enum class StudyStatusModel(val number: Int) {
    STUDY_STATUS_UNSPECIFIED(1),
    STUDY_STATUS_PARTICIPATING(2),
    STUDY_STATUS_WITHDRAW(3),
    STUDY_STATUS_DROP(4),
    STUDY_STATUS_COMPLETE(5);

    companion object {
        fun fromNumber(number: Int): StudyStatusModel = when (number) {
            STUDY_STATUS_UNSPECIFIED.number -> STUDY_STATUS_UNSPECIFIED
            STUDY_STATUS_PARTICIPATING.number -> STUDY_STATUS_PARTICIPATING
            STUDY_STATUS_WITHDRAW.number -> STUDY_STATUS_WITHDRAW
            STUDY_STATUS_DROP.number -> STUDY_STATUS_DROP
            STUDY_STATUS_COMPLETE.number -> STUDY_STATUS_COMPLETE
            else -> STUDY_STATUS_UNSPECIFIED
        }
    }
}

enum class StudyCategory {
    COMPLETED,
    REGISTERED
}
