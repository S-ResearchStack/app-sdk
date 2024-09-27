package researchstack.auth.domain.repository

import researchstack.auth.domain.model.Account

interface AccountRepository {
    suspend fun getAccount(): Account?
    suspend fun updateAccount(account: Account)
    suspend fun clearAccount()
}
