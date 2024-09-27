package researchstack.auth.data.datasource.auth.samsung

import android.os.Bundle
import com.msc.sa.aidl.ISACallback

open class ISACallbackStubAdapter : ISACallback.Stub() {
    override fun onReceiveAccessToken(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceiveChecklistValidation(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceiveDisclaimerAgreement(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceiveAuthCode(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceiveSCloudAccessToken(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceivePasswordConfirmation(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit

    override fun onReceiveRLControlFMM(requestID: Int, isSuccess: Boolean, resultData: Bundle?): Unit = Unit
}
