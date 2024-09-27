package researchstack.presentation.service.ipc

import aws.smithy.kotlin.runtime.time.Instant
import com.google.gson.annotations.SerializedName

internal open class IPCMessage(
    @SerializedName("Ts")
    val timestamp: Long,
    @SerializedName("MsgName")
    val messageName: String,
    @SerializedName("Note")
    val extras: String? = null,
)

internal data class EventRequest(
    @SerializedName("SessionId")
    val sessionId: String,
) {
    fun getStudyId(): String =
        sessionId.substring(
            0,
            sessionId.indexOfFirst { it == '-' }
        )
}

internal data class SubjectIdRequest(
    @SerializedName("StudyId")
    val studyId: String,
)

internal data class DataUploadRequest(
    @SerializedName("SessionId")
    val sessionId: String,
    @SerializedName("Uri")
    val uri: String,
) {
    fun getStudyId(): String =
        sessionId.substring(
            0,
            sessionId.indexOfFirst { it == '-' }
        )

    fun getUploadPath() = if (this.isDataTypeFile())
        "${getSubjectId()}/${getSessionTimeStamp()}/${getDataType()}/${getFileName()}"
    else "${getSubjectId()}/${getSessionTimeStamp()}/${getFileName()}"

    fun getFileName(): String = uri.substring(uri.lastIndexOf('/') + 1)

    private fun getSubjectId(): String = sessionId.split('-')[subjectIdIndex]

    private fun getDataType(): String = getFileName().split('-')[dataTypeIndex]

    private fun getSessionTimeStamp(): String = sessionId.split('-')[sessionTimestampIndex]

    private fun isDataTypeFile() = getFileName().endsWith(dataTypeFilePostfix)

    companion object {
        private const val subjectIdIndex = 1
        private const val dataTypeIndex = 2
        private const val sessionTimestampIndex = 2
        private const val dataTypeFilePostfix = "dutraw"
    }
}

internal class InvalidJsonErrorResponse(error: String) : IPCMessage(
    timestamp = Instant.now().epochSeconds,
    messageName = "Invalid Json",
    extras = """{"Note": "$error"}"""
)

internal data class DataUploadResponse(
    @SerializedName("SessionId")
    val sessionId: String,
    @SerializedName("Uri")
    val uri: String,
    @SerializedName("Result")
    val result: String,
    @SerializedName("Log")
    val log: String? = null,
)

internal data class SubjectIdResponse(
    @SerializedName("StudyId")
    val studyId: String,
    @SerializedName("SubjNum")
    val subjNum: String,
    @SerializedName("Result")
    val result: String,
)
