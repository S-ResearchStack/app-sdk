package researchstack.auth.data.repository.account

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.auth.data.datasource.local.pref.AccountPref
import researchstack.auth.domain.model.Account
import researchstack.auth.domain.repository.AccountRepository

class AccountPrefRepositoryImpl(
    private val accountPref: AccountPref,
) : AccountRepository {

    override suspend fun getAccount(): Account? = withContext(Dispatchers.IO) {
        accountPref.getAccount()
    }

    override suspend fun updateAccount(account: Account): Unit = withContext(Dispatchers.IO) {
        accountPref.updateAccount(account)
    }

    override suspend fun clearAccount(): Unit = withContext(Dispatchers.IO) {
        accountPref.clearAccount()
    }
}
