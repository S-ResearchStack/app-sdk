package researchstack.presentation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController =
    staticCompositionLocalOf<NavController> { noLocalProvidedFor("LocalNavController") }

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}
