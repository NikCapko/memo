package com.nikcapko.memo.presentation.words.list.ui

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.InsertEmoticon
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.presentation.R
import com.nikcapko.memo.presentation.words.list.state.WordListState

@Suppress("NonSkippableComposable")
@Composable
internal fun WordListScreen(
    state: State<WordListState>,
    onItemClick: (Int) -> Unit,
    onSpeakClick: (Int) -> Unit,
    addItemClick: () -> Unit,
    onRetryClick: () -> Unit,
    onGamesClick: () -> Unit,
    onClearDatabaseClick: () -> Unit,
) {
    when (state.value) {
        is WordListState.None,
        is WordListState.Loading,
            -> showLoading()

        is WordListState.Success -> {
            ShowContent(
                words = (state.value as WordListState.Success).words,
                onItemClick = onItemClick,
                onSpeakClick = onSpeakClick,
                addItemClick = addItemClick,
                onGamesClick = onGamesClick,
                onClearDatabaseClick = onClearDatabaseClick,
            )
        }

        is WordListState.Error -> {
            ShowError(
                error = (state.value as WordListState.Error).errorMessage,
                onRetry = onRetryClick,
            )
        }
    }
}

@Composable
internal fun showLoading() = Box(
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
fun ShowError(error: String, onRetry: () -> Unit) = Box(
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

@Suppress("NonSkippableComposable")
@Composable
internal fun ShowContent(
    words: List<WordModel>,
    onItemClick: (Int) -> Unit,
    onSpeakClick: (Int) -> Unit,
    addItemClick: () -> Unit,
    onGamesClick: () -> Unit,
    onClearDatabaseClick: () -> Unit,
) = Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier,
            onClick = addItemClick,
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                modifier = Modifier,
            )
        }
    },
    topBar = {
        TopAppBar(
            title = { Text("Memo") },
            actions = {
                IconButton(onClick = { onGamesClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.gamepad),
                        contentDescription = "Игры",
                        tint = MaterialTheme.colors.background,
                    )
                }
                IconButton(onClick = { onClearDatabaseClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Очистить",
                        tint = MaterialTheme.colors.background,
                    )
                }
            }
        )
    },
    content = (
        {
            if (words.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.InsertEmoticon,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colors.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Добавьте свое первое слово",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn {
                    itemsIndexed(words) { index, item ->
                        Row(
                            modifier = Modifier
                                .clickable { onItemClick(index) }
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 8.dp),
                                    text = item.word,
                                    color = MaterialTheme.colors.primary,
                                    fontStyle = FontStyle.Normal,
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 8.dp),
                                    text = item.translate,
                                    color = MaterialTheme.colors.secondary,
                                    fontStyle = FontStyle.Italic,
                                )
                            }
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .size(48.dp)
                                    .clickable { onSpeakClick(index) },
                                painter = painterResource(id = R.drawable.ic_sound_waves),
                                contentDescription = "speak",
                                tint = MaterialTheme.colors.primary,
                            )
                        }
                    }
                }
            }
        }
        )
)

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordListScreenPreviewContent() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            WordListState.Success(
                listOf(
                    WordModel(
                        id = 5537,
                        word = "reprehendunt",
                        translate = "euripidis",
                        frequency = 2.3f
                    )
                )
            )
        )
    }
    WordListScreen(
        state = state,
        onItemClick = { },
        onSpeakClick = { },
        addItemClick = { },
        onRetryClick = { },
        onGamesClick = { },
        onClearDatabaseClick = { },
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordListScreenPreviewEmpty() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            WordListState.Success(listOf<WordModel>())
        )
    }
    WordListScreen(
        state = state,
        onItemClick = { },
        onSpeakClick = { },
        addItemClick = { },
        onRetryClick = { },
        onGamesClick = { },
        onClearDatabaseClick = { },
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordListScreenPreviewLoading() = ComposeTheme {
    val state = remember {
        mutableStateOf(WordListState.Loading)
    }
    WordListScreen(
        state = state,
        onItemClick = { },
        onSpeakClick = { },
        addItemClick = { },
        onRetryClick = { },
        onGamesClick = { },
        onClearDatabaseClick = { },
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordListScreenPreviewError() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            WordListState.Error("Ошибка"),
        )
    }
    WordListScreen(
        state = state,
        onItemClick = { },
        onSpeakClick = { },
        addItemClick = { },
        onRetryClick = { },
        onGamesClick = { },
        onClearDatabaseClick = { },
    )
}
