package com.falcon.assignmentnewsapp.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.falcon.assignmentnewsapp.R
import com.falcon.assignmentnewsapp.Resource
import com.falcon.assignmentnewsapp.Utils
import com.falcon.assignmentnewsapp.modeels.Article
import com.falcon.assignmentnewsapp.viewmodels.NewsViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsListScreen(
    modalSheetState: ModalBottomSheetState,
    navController: NavHostController,
    changeCurrentNewsContent: (String) -> Unit,
) {
    val newsViewModel: NewsViewModel = hiltViewModel()
    val articles by newsViewModel.articles.collectAsState()
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
    }
    val editor = sharedPreferences.edit()
    editor.putBoolean(Utils.NEWUSER, false)
    editor.apply()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { _ ->
        Column {
            MainScreenHeader(navController)
            var searchQuery by remember { mutableStateOf("") }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            val pullRefreshState = rememberPullRefreshState(
                refreshing = articles is Resource.Loading,
                onRefresh = {
                    newsViewModel.fetchNews()
                }
            ) // TODO: USELESSSSSSSSSS NOT BORKING
            fun getContent(url: String) {
                newsViewModel.getDataFromUrl(url) { content ->
                    Log.i("kaali billi", content)
                    changeCurrentNewsContent(content)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .pullRefresh(pullRefreshState) // TODO: Removed Refresh feature because it's crashing
            ) {
                Log.i("NewsListScreen", "Before Entering When Statement, Class Name:" + articles.javaClass.simpleName)
                when (articles) {
                    is Resource.Loading -> {
                        ShimmerEffect()
                    }
                    is Resource.Success -> {
                        Log.i("NewsListScreen", "Inside Resource.Success Block, Class Name:" + articles.javaClass.simpleName)
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            val filteredNews = (articles as Resource.Success<List<Article>>).data.filter {
                                it.title?.contains(searchQuery, true) ?: false
                            }
                            items(filteredNews) { articleItem ->
                                NoticeItem(
                                    article = articleItem,
                                    modalSheetState = modalSheetState,
                                    getContent = ::getContent
                                )
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = (articles as Resource.Error).message,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
//                PullRefreshIndicator(
//                    refreshing = articles is Resource.Loading,
//                    state = pullRefreshState,
//                    modifier = Modifier.align(Alignment.TopCenter)
//                ) // todo: yeh kyu nhi chl rrha, imposible glitch hai yeh toh
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun NoticeItem(
    article: Article,
    modalSheetState: ModalBottomSheetState,
    getContent: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(4.dp, 2.dp, 4.dp, 2.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.white))
                    .padding(8.dp, 8.dp, 8.dp, 2.dp)
                    .clickable {
                        getContent(article.url.toString())
                        scope.launch {
                            modalSheetState.show()
                        }
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notes_blue),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CutCornerShape(CornerSize(3.dp))),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    androidx.compose.material.Text(
                        text = article.title ?: "UNKNOWN TITLE",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold_1)),
                    )
                }

            }
        }
    )
}

@Composable
private fun MainScreenHeader(
    navController: NavHostController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 24.dp, 8.dp, 0.dp)
    ) {
        Text(
            text = "ASSIGNMENT NEWS",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.nunito_bold_1)),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )
        Image(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier
                .size(26.dp)
                .clickable {
                    navController.navigate("settings")
//                    TODO: HANDLE SETTINGS
                }
        )
    }
}

@Composable
fun ShimmerEffect() {
    Column {
        repeat(10) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(80.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = Color.LightGray
                    )
            ) {}
        }
    }
}