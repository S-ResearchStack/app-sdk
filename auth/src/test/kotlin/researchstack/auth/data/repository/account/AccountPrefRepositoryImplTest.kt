package researchstack.data.repository.account

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.auth.POSITIVE_TEST
import researchstack.auth.data.datasource.local.pref.AccountPref
import researchstack.auth.data.repository.account.AccountPrefRepositoryImpl
import researchstack.auth.domain.model.Account
import researchstack.data.local.pref.DataStoreTestHelper

internal class AccountPrefRepositoryImplTest : DataStoreTestHelper("account.preferences_pb") {

    private val accountPrefRepository =
        AccountPrefRepositoryImpl(AccountPref(testDataStore))

    @Test
    @Tag(POSITIVE_TEST)
    fun `get-set-clear`() = testScope.runTest {
        val account = Account("account-id", "idp")
        accountPrefRepository.updateAccount(account)

        assertEquals(account, accountPrefRepository.getAccount())

        accountPrefRepository.clearAccount()
        assertNull(accountPrefRepository.getAccount())
    }
}
