package com.nikcapko.memo.presentation.words.list

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikcapko.core.viewmodel.DataLoadingViewModelState
import com.nikcapko.memo.core.data.Word
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.presentation.R

@Suppress("NonSkippableComposable")
@Composable
internal fun WordListScreen(
    state: State<DataLoadingViewModelState?>,
    onItemClick: (Int) -> Unit,
    onSpeakClick: (Int) -> Unit,
    addItemClick: () -> Unit,
) {
    when (state.value) {
        DataLoadingViewModelState.NoneState, DataLoadingViewModelState.LoadingState -> showLoading()
        DataLoadingViewModelState.NoItemsState -> ShowWords(
            emptyList(),
            onItemClick,
            onSpeakClick,
            addItemClick
        )

        is DataLoadingViewModelState.LoadedState<*> -> {
            ((state.value as DataLoadingViewModelState.LoadedState<*>).data as? List<*>)?.filterIsInstance<Word>()
                ?.let {
                    ShowWords(it, onItemClick, onSpeakClick, addItemClick)
                }
        }

        is DataLoadingViewModelState.ErrorState -> ShowError()
        null -> Unit
    }
}

@Composable
internal fun showLoading() = Box {}

@Suppress("NonSkippableComposable")
@Composable
internal fun ShowWords(
    words: List<Word>,
    onItemClick: (Int) -> Unit,
    onSpeakClick: (Int) -> Unit,
    addItemClick: () -> Unit,
) = Scaffold(
    backgroundColor = colors.background,
    floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier,
            onClick = addItemClick
        ) {
            Icon(Icons.Filled.Add, "")
        }
    },
    content = ({
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
                            color = colors.primary,
                            fontStyle = FontStyle.Normal,
                        )
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 8.dp),
                            text = item.translation,
                            color = colors.secondary,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .size(48.dp)
                            .clickable { onSpeakClick(index) },
                        painter = painterResource(id = R.drawable.ic_sound_waves),
                        contentDescription = "speak",
                        colorFilter = ColorFilter.tint(colors.primary),
                    )
                }
            }
        }
    })
)

@Preview(showSystemUi = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun WordListScreenPreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            DataLoadingViewModelState.LoadedState(
                listOf(
                    Word(
                        id = 5537,
                        word = "reprehendunt",
                        translation = "euripidis",
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
    )
}

@Composable
fun ShowError() = Box {
}
