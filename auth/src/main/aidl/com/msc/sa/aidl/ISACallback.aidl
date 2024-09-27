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

/**
* AIDL Interface Class.
* Provide callback functions.
*/
interface ISACallback {

     /**
    *  receive requestAccessToken callback data.
    */
    void onReceiveAccessToken(int requestID, boolean isSuccess,  in Bundle resultData);

    /**
    *  receive requestChecklistValidation callback data.
    */
    void onReceiveChecklistValidation(int requestID, boolean isSuccess, in Bundle resultData);

    /**
    *  receive requestDisclaimerAgreement callback data.
    */
    void onReceiveDisclaimerAgreement(int requestID, boolean isSuccess, in Bundle resultData);

    /**
    *  receive requestAuthCode callback data.
    */
    void onReceiveAuthCode(int requestID, boolean isSuccess,  in Bundle resultData);

    /**
    *  receive requestSCloudAccessToken callback data.
    */
    void onReceiveSCloudAccessToken(int requestID, boolean isSuccess,  in Bundle resultData);

    /**
    *  receive requestPasswordConfirmation callback data.
    */
    void onReceivePasswordConfirmation(int requestID, boolean isSuccess,  in Bundle resultData);

    /**
    *  receive requestRLControlFMM callback data.
    */
    void onReceiveRLControlFMM(int requestID, boolean isSuccess,  in Bundle resultData);

}
