package researchstack.data.local.room.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.NEGATIVE_TEST

class PrivDaoTest {

    private fun Class<*>.getHelper(): PrivDaoTestHelper<*> = when (this) {
        AccelerometerDao::class.java -> AccelerometerDaoTestHelper()
        BiaDao::class.java -> BiaDaoTestHelper()
        EcgDao::class.java -> EcgDaoTestHelper()
        HeartRateDao::class.java -> HeartRateDaoTestHelper()
        PpgGreenDao::class.java -> PpgGreenDaoTestHelper()
        PpgIrDao::class.java -> PpgIrDaoTestHelper()
        PpgRedDao::class.java -> PpgRedDaoTestHelper()
        SpO2Dao::class.java -> SpO2DaoTestHelper()
        SweatLossDao::class.java -> SweatLossDaoTestHelper()
        else -> throw Exception()
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, BiaDao::class, EcgDao::class, HeartRateDao::class, PpgGreenDao::class, PpgIrDao::class, PpgRedDao::class, SpO2Dao::class, SweatLossDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `insert_should_return_null`(clazz: Class<*>) {
        Assertions.assertNull(clazz.getHelper().`insert_should_return_null`())
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, BiaDao::class, EcgDao::class, HeartRateDao::class, PpgGreenDao::class, PpgIrDao::class, PpgRedDao::class, SpO2Dao::class, SweatLossDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `insertAll_should_return_null`(clazz: Class<*>) {
        Assertions.assertNull(clazz.getHelper().`insertAll_should_return_null`())
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, BiaDao::class, EcgDao::class, HeartRateDao::class, PpgGreenDao::class, PpgIrDao::class, PpgRedDao::class, SpO2Dao::class, SweatLossDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `deleteLEThan_should_return_null`() {
        Assertions.assertThrows(NullPointerException::class.java) {
            AccelerometerDaoTestHelper().`deleteLEThan_should_throw_NullPointerException`()
        }
    }
}
