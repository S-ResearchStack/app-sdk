/*
 * Copyright (C) 2019 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */
package com.msc.sa.aidl;

import com.msc.sa.aidl.ISACallback;

/**
* AIDL Interface Class.
* Provide Register Callback, unRegisterCallback, requestAccesToken requestChecklistValidation, requestDisclaimerAgreement, requestAuthCode, requestSCloudAccessToken, requestPasswordConfirmation, requestRLControlFMM, requestRubinRequest Function.
*/
interface ISAService {

    /**
    * register Service Appicaion Callback and Client info.
    */
    String registerCallback(String clientID, String clientSecret, String packageName, ISACallback callback);

    /**
    * unregister Service Appicaion Callback.
    */
    boolean unregisterCallback(String registrationCode);

    /**
    * request AccessToken.
    */
    boolean requestAccessToken(int requestID, String registrationCode, in Bundle data);

    /**
    * request Checklist Validation.
    */
    boolean requestChecklistValidation(int requestID, String registrationCode, in Bundle data);

    /**
    * request Disclaimer Agreement.
    */
    boolean requestDisclaimerAgreement(int requestID, String registrationCode, in Bundle data);

    /**
    * request AuthCode.
    */
    boolean requestAuthCode(int requestID, String registrationCode, in Bundle data);

    /**
    * request SCloud AccessToken.
    */
    boolean requestSCloudAccessToken(int requestID, String registrationCode, in Bundle data);

    /**
    * request password Confirmation.
    */
    boolean requestPasswordConfirmation(int requestID, String registrationCode, in Bundle data);

    /**
    * request enable/disable Reactivation lock by FMM.
    */
    boolean requestRLControlFMM(int requestID, String registrationCode, in Bundle data);

    /**
     * requests Rubin turn on/off
     */
    boolean requestRubinRequest(int requestID, String registrationcode, in Bundle data);
}
