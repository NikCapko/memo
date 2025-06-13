package com.nikcapko.memo.presentation.screens.games.findpairs.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.screens.games.findpairs.state.FindPairsState

@Composable
internal fun FindPairsScreen(
    state: State<FindPairsState>,
    onRetry: () -> Unit,
    onWordClick: (FindPairsState.Item) -> Unit,
    onTranslateClick: (FindPairsState.Item) -> Unit,
    endGameClick: () -> Unit,
) {
    when (val stateValue = state.value) {
        is FindPairsState.None -> Unit
        is FindPairsState.Loading -> ShowLoading()
        is FindPairsState.Error -> ShowError(stateValue.errorMessage, onRetry)
        is FindPairsState.Success -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .weight(1f),
                ) {
                    itemsIndexed(stateValue.wordList) { index, item ->
                        Button(
                            onClick = { onWordClick(item) },
                            colors = if (item.checked) {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                )
                            } else {
                                ButtonDefaults.buttonColors()
                            },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(all = 8.dp)
                                .height(56.dp)
                                .then(
                                    if (!item.isVisible) {
                                        Modifier
                                            .alpha(0f)
                                            .pointerInput(Unit) {}
                                    } else {
                                        Modifier
                                    }
                                ),
                        ) {
                            Text(item.value)
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .weight(1f),
                ) {
                    itemsIndexed(stateValue.translateList) { index, item ->
                        Button(
                            onClick = { onTranslateClick(item) },
                            colors = if (item.checked) {
                                ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.secondary,
                                )
                            } else {
                                ButtonDefaults.buttonColors()
                            },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(all = 8.dp)
                                .height(56.dp)
                                .then(
                                    if (!item.isVisible) {
                                        Modifier
                                            .alpha(0f)
                                            .pointerInput(Unit) {}
                                    } else {
                                        Modifier
                                    }
                                ),
                        ) {
                            Text(item.value)
                        }
                    }
                }
            }
        }

        is FindPairsState.EndGame -> ShowEndGame(endGameClick)
    }
}

@Composable
private fun ShowLoading() = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
    contentAlignment = Alignment.Center,
) {
    CircularProgressIndicator(
        modifier = Modifier.size(50.dp),
        color = MaterialTheme.colors.primary
    )
}

@Composable
private fun ShowError(error: String, onRetry: () -> Unit) = Box(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background),
    contentAlignment = Alignment.Center,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Попробовать еще раз")
        }
    }
}

@Composable
fun ShowEndGame(endGameClick: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                LottieAnimation(
                    composition = composition,
                    speed = 2f,
                    clipSpec = LottieClipSpec.Progress(0.1f, 1f),
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = "Игра успешно окончена",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Button(
            onClick = endGameClick,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(all = 16.dp)
                .height(56.dp),
        ) {
            Text("Закончить игру")
        }
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun FindPairsScreenPreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            FindPairsState.Success(
                wordList = listOf(
                    FindPairsState.Item(
                        id = "1",
                        value = "1",
                    ),
                    FindPairsState.Item(
                        id = "2",
                        value = "2",
                        checked = true,
                    ),
                    FindPairsState.Item(
                        id = "3",
                        value = "3",
                    ),
                ),
                translateList = listOf(
                    FindPairsState.Item(
                        id = "1",
                        value = "11",
                    ),
                    FindPairsState.Item(
                        id = "2",
                        value = "22",
                    ),
                    FindPairsState.Item(
                        id = "3",
                        value = "33",
                        checked = true,
                    ),
                ),
            )
        )
    }
    FindPairsScreen(
        state = state,
        onRetry = {},
        onWordClick = {},
        onTranslateClick = {},
        endGameClick = {},
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun FindPairsScreenEndGamePreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            FindPairsState.EndGame
        )
    }
    FindPairsScreen(
        state = state,
        onRetry = {},
        onWordClick = {},
        onTranslateClick = {},
        endGameClick = {},
    )
}
