package researchstack.data.datasource.grpc.mapper

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.SubjectStatus
import researchstack.domain.model.StudyStatusModel

class UserStatusGrpcMapperTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `UserStatus#toDomain use should not throw Exception`() {
        SubjectStatus.values().forEach {
            Assertions.assertNotNull(it.toDomain())
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `UserStatus#toDomain use should return STUDY_STATUS_UNSPECIFIED`() {
        Assertions.assertEquals(SubjectStatus.UNRECOGNIZED.toDomain(), StudyStatusModel.STUDY_STATUS_UNSPECIFIED)
    }
}
