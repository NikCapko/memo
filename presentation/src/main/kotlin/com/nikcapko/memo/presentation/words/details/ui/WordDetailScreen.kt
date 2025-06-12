package com.nikcapko.memo.presentation.words.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikcapko.memo.presentation.words.details.state.WordDetailsState

@Composable
internal fun WordDetailScreen(
    state: State<WordDetailsState>,
    changeWordField: (String) -> Unit,
    changeTranslateField: (String) -> Unit,
    onAddWord: () -> Unit,
    onChangeWord: () -> Unit,
    onDeleteWord: () -> Unit,
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
        content = (
            {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                ) {
                    TextField(
                        value = state.value.word.word,
                        onValueChange = { changeWordField(it) },
                        label = { Text("Слово") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = state.value.word.translate,
                        onValueChange = { changeTranslateField(it) },
                        label = { Text("Перевод") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            ),
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding(),
            ) {
                if (state.value.isAddNewWord) {
                    Button(
                        onClick = { onAddWord() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.value.isEnableAddButton,
                    ) {
                        Text("Добавить")
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onChangeWord() },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Изменить")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { onDeleteWord() },
                            modifier = Modifier.weight(1f),
                            enabled = state.value.isEnableAddButton,
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    )
}
