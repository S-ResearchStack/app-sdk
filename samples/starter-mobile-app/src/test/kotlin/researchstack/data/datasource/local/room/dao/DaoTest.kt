package researchstack.data.datasource.local.room.dao

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.NEGATIVE_TEST

class DaoTest {

    private fun Class<*>.getHelper(): TimestampEntityBaseDaoTestHelper<*> = when (this) {
        AccelerometerDao::class.java -> AccelerometerDaoTestHelper()
        LightDao::class.java -> LightDaoTestHelper()
        SpeedDao::class.java -> SpeedDaoTestHelper()
        else -> throw Exception()
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, LightDao::class, SpeedDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `insert should return null`(clazz: Class<*>) {
        Assertions.assertNull(clazz.getHelper().`insert should return null`())
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, LightDao::class, SpeedDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `insertAll should return null`(clazz: Class<*>) {
        Assertions.assertNull(clazz.getHelper().`insertAll should return null`())
    }

    @ParameterizedTest
    @ValueSource(
        classes = [AccelerometerDao::class, LightDao::class, SpeedDao::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `deleteLEThan should return null`(clazz: Class<*>) {
        Assertions.assertThrows(NullPointerException::class.java) {
            clazz.getHelper().`deleteLEThan should throw NullPointerException`()
        }
    }
}
