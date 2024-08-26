package com.falcon.assignmentnewsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.falcon.assignmentnewsapp.ui.theme.AssignmentNewsAppTheme
import kotlinx.coroutines.launch

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
            val context = LocalContext.current
            val sharedPreferences = remember {
                context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
            }
            val currentNewsContent = remember {
                mutableStateOf("Type")
            }
            NavHost(navController = navController, startDestination = "main_page") {
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
                            MainScreenBottomSheetContent(modalSheetState, navController, currentNewsContent)
                        }
                    ) {
                        NoticeListScreen(
                            openFile = ::openFile,
                            shareFile = ::shareFile,
                            modalSheetState = modalSheetState,
                            fcmNoticeIdList = fcmNoticeId,
                            navController = navController
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
    navController: NavHostController
) {
    DrawerContent(navController, modalSheetState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContent(navController: NavHostController, modalSheetState: ModalBottomSheetState) {
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                text = "Menu",
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
        // TODO: ADD TEXT
    }

}