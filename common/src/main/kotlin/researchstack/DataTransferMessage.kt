package researchstack

import researchstack.domain.model.priv.PrivDataType

data class DataTransferMessage(
    val dataType: PrivDataType,
    val data: Any,
)
