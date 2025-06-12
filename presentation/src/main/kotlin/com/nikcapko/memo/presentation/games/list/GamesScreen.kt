package com.nikcapko.memo.presentation.games.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikcapko.domain.model.Game
import com.nikcapko.memo.core.ui.theme.ComposeTheme

@Composable
internal fun GamesScreen(
    state: State<GamesState>,
    onItemClicked: (Game) -> Unit,
    onBackPressed: () -> Unit,
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("Memo") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
            )
        },
        content = ({
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                itemsIndexed(state.value.games) { index, item ->
                    Row(
                        modifier = Modifier
                            .clickable { onItemClicked(item) }
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                            text = item.title,
                            color = MaterialTheme.colors.primary,
                            fontStyle = FontStyle.Normal,
                        )
                    }
                }
            }
        }),
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordListScreenPreviewContent() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            GamesState(
                games = listOf(
                    Game.SELECT_TRANSLATE,
                    Game.FIND_PAIRS,
                )
            )
        )
    }
    GamesScreen(
        state = state,
        onItemClicked = { },
        onBackPressed = { },
    )
}
