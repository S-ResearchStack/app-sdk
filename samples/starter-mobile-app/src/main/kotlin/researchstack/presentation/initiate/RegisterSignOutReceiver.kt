package researchstack.presentation.initiate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.viewmodel.SignOutViewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private const val TAG = "SAMSUNGACCOUNT_SIGNOUT_BROADCAST"

@Composable
fun RegisterSignOutReceiver(
    navController: NavController,
    signOutViewModel: SignOutViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    DisposableEffect(null) {
        val saSignOutReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) = goAsync(Dispatchers.IO) {
                if (intent.action.equals("com.samsung.account.SAMSUNGACCOUNT_SIGNOUT_COMPLETED")) {
                    Log.i(TAG, "HEALTH RESEARCH APP Received SAMSUNGACCOUNT_SIGNOUT_COMPLETED")

                    signOutViewModel.signOut()
                    navigateToWelcomeScreen()
                }
            }

            private fun BroadcastReceiver.goAsync(
                context: CoroutineContext = EmptyCoroutineContext,
                block: suspend CoroutineScope.() -> Unit,
            ) {
                val pendingResult = goAsync()
                @OptIn(DelicateCoroutinesApi::class)
                GlobalScope.launch(context) {
                    try {
                        block()
                    } finally {
                        pendingResult.finish()
                    }
                }
            }

            private fun navigateToWelcomeScreen() {
                CoroutineScope(Dispatchers.Main).launch {
                    if (navController.currentDestination?.route != Route.Welcome.name) {
                        navController.popBackStack(Route.Welcome.name, false)
                    } else {
                        navController.popBackStack()
                    }
                    navController.navigate(Route.Welcome.name)
                }
            }
        }

        val intentFilter = IntentFilter("com.samsung.account.SAMSUNGACCOUNT_SIGNOUT_COMPLETED")
        context.registerReceiver(saSignOutReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        onDispose {
            context.unregisterReceiver(saSignOutReceiver)
        }
    }
}
