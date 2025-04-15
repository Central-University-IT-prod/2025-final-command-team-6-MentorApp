package com.prodmobile.template.feature.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.prodmobile.template.core.Constants.mentorSkillsSuggestions

@Composable
fun MentorSkillsPage(
    modifier: Modifier = Modifier,
    onDataSubmitted: (List<String>) -> Unit,
) {
    val fieldsState = remember { mutableStateListOf(FieldState()) }
    var triedContinue by remember { mutableStateOf(false) }
    val isError = fieldsState.none { it.textFieldValue.text.isNotBlank() }

    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            Text(
                text = "Добавь свои\nсильные стороны",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 32.dp)
            )
        }

        itemsIndexed(fieldsState) { index, fieldState ->
            MentorSkillDropDownTextField(
                isFirst = index == 0,
                fieldState = fieldState,
                onDelete = { fieldsState.removeAt(index) },
                onClearField = {
                    fieldsState[index].textFieldValue = TextFieldValue(text = "")
                },
                label = "Навык ${index + 1}",
                suggestions = mentorSkillsSuggestions,
                isError = triedContinue && isError
            )
        }

        item {
            MentorContinueFromSkillsPageButtons(
                modifier = Modifier
                    .imePadding(),
                onAddSkill = {
                    fieldsState.add(FieldState())
                },
                onContinue = {
                    if (!isError) {
                        onDataSubmitted(
                            fieldsState
                                .map {
                                    it.textFieldValue.text
                                }
                                .filter { it.isNotBlank() }
                        )
                    } else {
                        triedContinue = true
                    }
                }
            )
        }
    }
}

@Composable
private fun LazyItemScope.MentorContinueFromSkillsPageButtons(
    modifier: Modifier = Modifier,
    onAddSkill: () -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillParentMaxWidth()
            .padding(end = 8.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = onAddSkill)
                .padding(4.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Text(
                text = "Добавить навык",
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .clickable(onClick = onContinue)
                .padding(4.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
            Text(
                text = "Продолжить",
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun MentorSkillDropDownTextField(
    isError: Boolean,
    isFirst: Boolean,
    fieldState: FieldState,
    onDelete: () -> Unit,
    onClearField: () -> Unit,
    label: String,
    suggestions: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Column {
            OutlinedTextField(
                value = fieldState.textFieldValue,
                onValueChange = {
                    fieldState.textFieldValue = it
                    if (suggestions.none { suggestion ->
                            it.text == suggestion
                        }
                    )
                        fieldState.expanded = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        fieldState.textFieldSize = coordinates.size.toSize()
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White.copy(0.6f),
                    unfocusedIndicatorColor = Color.Gray,
                    errorContainerColor = Color.Transparent,
                    errorIndicatorColor = MaterialTheme.colorScheme.error,
                ),
                isError = isError,
                trailingIcon = {
                    Row {
                        IconButton(onClick = { fieldState.expanded = !fieldState.expanded }) {
                            Icon(
                                imageVector = if (fieldState.expanded) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle dropdown"
                            )
                        }

                        if (fieldState.textFieldValue.text.isNotEmpty()) {
                            IconButton(onClick = onClearField) {
                                Icon(Icons.Default.ClearAll, contentDescription = "Clear field")
                            }
                        } else if (!isFirst) {
                            IconButton(onClick = onDelete) {
                                Icon(Icons.Default.Close, contentDescription = "Delete field")
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(Modifier.height(8.dp))

            DropdownList(fieldState, suggestions)
        }
    }
}