package com.nikcapko.memo.presentation.screens.words.details.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nikcapko.domain.model.WordModel
import com.nikcapko.memo.core.ui.theme.ComposeTheme
import com.nikcapko.memo.presentation.screens.words.details.state.WordDetailsState

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
    val focusRequester2 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { },
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
                        .padding(16.dp)
                        .clickable(
                            onClick = { keyboardController?.hide() },
                            indication = null,
                            interactionSource = interactionSource,
                        ),
                ) {
                    TextField(
                        value = state.value.word.word,
                        keyboardActions = KeyboardActions(
                            onNext = { focusRequester2.requestFocus() },
                        ),
                        onValueChange = { changeWordField(it) },
                        label = { Text("Слово") },
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                        ),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = state.value.word.translate,
                        onValueChange = { changeTranslateField(it) },
                        label = { Text("Перевод") },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester2),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        contentPadding = ButtonDefaults.ContentPadding,
                        enabled = state.value.enableSaveButton,
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
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            enabled = state.value.enableSaveButton,
                        ) {
                            Text("Изменить")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { onDeleteWord() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            contentPadding = ButtonDefaults.ContentPadding,
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            }
        }
    )
}

@Preview(device = Devices.NEXUS_5)
@Composable
private fun WordDetailScreenPreview() = ComposeTheme {
    val state = remember {
        mutableStateOf(
            WordDetailsState(
                word = WordModel(
                    id = 5537,
                    word = "reprehendunt",
                    translate = "euripidis",
                    frequency = 2.3f
                ),
                isAddNewWord = false,
                showProgressDialog = false,
                enableSaveButton = true,
            )
        )
    }
    WordDetailScreen(
        state = state,
        changeWordField = {},
        changeTranslateField = {},
        onAddWord = {},
        onChangeWord = {},
        onDeleteWord = {},
        onBackPressed = {},
    )
}
