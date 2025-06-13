package com.nikcapko.memo.presentation.screens.games.selecttranslate.ui

import androidx.annotation.RawRes
import androidx.compose.foundation.Image
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.screens.games.selecttranslate.state.SelectTranslateScreenState
import com.nikcapko.memo.presentation.screens.games.selecttranslate.state.SelectTranslateState

@Composable
internal fun SelectTranslateScreen(
    state: State<SelectTranslateState>,
    onRetry: () -> Unit,
    onTranslateClick: (String) -> Unit,
    animationEnd: () -> Unit,
    endGameClick: () -> Unit,
) {
    when (val stateValue = state.value) {
        is SelectTranslateState.Error -> ShowError(
            error = stateValue.errorMessage,
            onRetry = onRetry,
        )

        is SelectTranslateState.Loading -> ShowLoading()
        is SelectTranslateState.None -> Unit
        is SelectTranslateState.Success -> when (val screenState = stateValue.screenState) {
            is SelectTranslateScreenState.None -> Unit
            is SelectTranslateScreenState.SelectWord -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(MaterialTheme.colors.background),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = screenState.word.word,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6,
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                    ) {
                        itemsIndexed(screenState.translates) { index, item ->
                            Button(
                                onClick = { onTranslateClick(item) },
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(all = 8.dp)
                                    .height(56.dp),
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }

            is SelectTranslateScreenState.Success ->
                SelectTranslateAnimation(
                    animation = R.raw.success,
                    onAnimationFinished = animationEnd,
                )

            is SelectTranslateScreenState.Error ->
                SelectTranslateAnimation(
                    animation = R.raw.error,
                    onAnimationFinished = animationEnd,
                )

            is SelectTranslateScreenState.Result ->
                SelectTranslateEndGame(screenState.successCount, screenState.errorCount, endGameClick)
        }
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
private fun SelectTranslateAnimation(
    @RawRes animation: Int,
    onAnimationFinished: () -> Unit,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
    val animatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        if (composition != null) {
            animatable.animate(
                composition = composition,
                iterations = 1,
                initialProgress = 0.1f,
                speed = 2f,
            )
            onAnimationFinished()
        }
    }

    LottieAnimation(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        composition = composition,
        progress = { animatable.progress },
    )
}

@Composable
private fun SelectTranslateEndGame(
    successCount: Int,
    errorCount: Int,
    endGameClick: () -> Unit,
) {
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
                Text(
                    text = "Ваши результаты:",
                    style = MaterialTheme.typography.h6,
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Успешно: $successCount",
                        style = MaterialTheme.typography.body1,
                    )
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 8.dp),
                        painter = painterResource(id = R.drawable.ic_success),
                        contentDescription = "",
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Ошибок: $errorCount",
                        style = MaterialTheme.typography.body1,
                    )
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(start = 8.dp),
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = "",
                    )
                }
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
private fun SelectTranslateScreenSelectWordPreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            SelectTranslateState.Success(
                screenState = SelectTranslateScreenState.SelectWord(
                    word = WordModel(
                        id = 3802,
                        word = "aliquip",
                        translate = "per",
                        frequency = 2.3f,
                    ),
                    translates = listOf("1", "2", "3", "4", "5")
                ),
                words = emptyList(),
            )
        )
    }
    SelectTranslateScreen(
        state = state,
        onRetry = {},
        onTranslateClick = {},
        animationEnd = {},
        endGameClick = {},
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun SelectTranslateScreenSuccessAnimationPreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            SelectTranslateState.Success(
                screenState = SelectTranslateScreenState.Success,
                words = emptyList(),
            )
        )
    }
    SelectTranslateScreen(
        state = state,
        onRetry = {},
        onTranslateClick = {},
        animationEnd = {},
        endGameClick = {},
    )
}


@Preview(device = Devices.NEXUS_5)
@Composable
private fun SelectTranslateScreenEndGamePreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            SelectTranslateState.Success(
                screenState = SelectTranslateScreenState.Result(successCount = 2, errorCount = 3),
                words = emptyList(),
            )
        )
    }
    SelectTranslateScreen(
        state = state,
        onRetry = {},
        onTranslateClick = {},
        animationEnd = {},
        endGameClick = {},
    )
}
