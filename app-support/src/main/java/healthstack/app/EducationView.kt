package healthstack.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import healthstack.app.pref.AppStage
import healthstack.kit.info.publication.Publication
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomBarNavigation
import healthstack.kit.ui.BottomNavItem

class EducationView(
    val changeNavigation: (AppStage) -> Unit,
    private val publications: List<Publication>, // TODO: publications should be updated & received from DB
) {
    @Composable
    fun Render() {
        val scrollState = rememberScrollState()
        var selectedArticle by remember {
            mutableStateOf<Publication?>(null)
        }
        if (selectedArticle == null) {
            Scaffold(
                drawerElevation = 0.dp,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Education",
                                style = AppTheme.typography.headline3,
                                color = AppTheme.colors.onBackground,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        },
                        backgroundColor = AppTheme.colors.background,
                        elevation = 0.dp
                    )
                },
                bottomBar = {
                    BottomBarNavigation(
                        listOf(
                            BottomNavItem("Home", Icons.Default.Home) {
                                changeNavigation(AppStage.Home)
                            },
                            BottomNavItem("Education", Icons.Default.MenuBook) {
                                changeNavigation(AppStage.Education)
                            }
                        ),
                        1
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = AppTheme.colors.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    for (publication in publications) {
                        Spacer(modifier = Modifier.height(8.dp))
                        publication.CardView {
                            selectedArticle = it
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            selectedArticle!!.Render { selectedArticle = it }
        }
    }
}
