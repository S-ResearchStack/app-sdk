package researchstack.domain.model.shealth

import researchstack.backend.integration.HealthDataModelInterface

data class HealthDataModel(
    val unifiedDataType: HealthTypeEnum,
    val dataList: List<Map<String, Any>>
) : HealthDataModelInterface
