package researchstack.auth.data.datasource.auth.samsung

interface IdTokenListener {
    fun onReceiveTokenResult(result: Result<String>)
}
