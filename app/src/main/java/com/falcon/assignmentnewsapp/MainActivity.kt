package com.falcon.assignmentnewsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.falcon.assignmentnewsapp.screens.NewsListScreen
import com.falcon.assignmentnewsapp.screens.WelcomePag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // Sir, I Removed this because status bar show nhi kr rrha tha
        setContent {
            val navController = rememberNavController()
            val modalSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
                skipHalfExpanded = true
            )
            var currentNewsContent by remember {
                mutableStateOf("Loading Please Wait......")
            }
            fun changeCurrentNewsContent(contentOfCurrentNews: String) {
                currentNewsContent = contentOfCurrentNews
            }
            val context = LocalContext.current
            val sharedPreferences = remember {
                context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
            }
            val isNewUser = sharedPreferences.getBoolean(Utils.NEWUSER, true)
            val startDestination = if (isNewUser) "welcome_page" else "main_screen"
            NavHost(navController = navController, startDestination = startDestination) {
                composable("welcome_page") {
                    BackHandler(
                        onBack = {
                            finish()
                        }
                    )
                    WelcomePag(navController)
                }
                composable("main_screen") {
                    BackHandler(
                        onBack = {
                            finish()
                        }
                    )
                    ModalBottomSheetLayout(
                        sheetState = modalSheetState,
                        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        sheetContent = {
                            MainScreenBottomSheetContent(modalSheetState, currentNewsContent)
                        }
                    , content = {
                        NewsListScreen(
                            modalSheetState = modalSheetState,
                            navController = navController,
                            changeCurrentNewsContent = ::changeCurrentNewsContent
                        )
                    }
                    )
                }
                composable("settings") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        androidx.compose.material.Text(
                            text = "Kitna Expect Kroge Sir :)",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_bold_1)),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenBottomSheetContent(
    modalSheetState: ModalBottomSheetState,
    currentNewsContent: String,
) {
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Author",
                style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                fontSize = 20.sp
            )
            androidx.compose.material.Icon(
                Icons.Filled.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .clickable {
                        scope.launch { modalSheetState.hide() }
                    }
            )
        }
        androidx.compose.material.Text(
            text = currentNewsContent,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.nunito_bold_1)),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}