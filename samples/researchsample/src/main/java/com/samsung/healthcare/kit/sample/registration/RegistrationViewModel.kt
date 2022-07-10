package com.samsung.healthcare.kit.sample.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.samsung.healthcare.kit.external.data.User
import com.samsung.healthcare.kit.external.network.ResearchPlatformAdapter
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Failed
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Loading
import com.samsung.healthcare.kit.sample.registration.RegistrationState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Init)

    val state: StateFlow<RegistrationState> = _state

    fun registerUser(profile: Map<String, Any>) {
        _state.value = Loading
        auth.currentUser?.getIdToken(false)
            ?.addOnSuccessListener { result ->
                result.token?.let { idToken ->
                    viewModelScope.launch {
                        try {
                            ResearchPlatformAdapter.getInstance()
                                .registerUser(idToken, User(auth.uid!!, profile))
                            _state.value = Success
                            // TODO handle specfic exception
                        } catch (e: Exception) {
                            Log.d(RegistrationViewModel::class.simpleName, "fail to register user")
                            _state.value = Failed
                            e.printStackTrace()
                        }
                    }
                }
            }?.addOnFailureListener {
                Log.d(RegistrationViewModel::class.simpleName, "fail to get id token")
                _state.value = Failed
            }
    }
}
