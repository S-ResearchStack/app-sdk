package researchstack.wearable.standalone.domain.exception

object AlreadyExistedUserException : RuntimeException() {
    override fun getLocalizedMessage(): String {
        // TODO how to handle getLocalizedMessage
        return "없습니다 유저"
    }
}
