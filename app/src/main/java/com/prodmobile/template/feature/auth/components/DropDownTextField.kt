package com.prodmobile.template.feature.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class FieldState(
    text: String = "",
    expanded: Boolean = false,
    textFieldSize: Size = Size.Zero
) {
    var textFieldValue by mutableStateOf(TextFieldValue(text, TextRange(text.length)))
    var expanded by mutableStateOf(expanded)
    var textFieldSize by mutableStateOf(textFieldSize)
}

@Composable
fun DropdownList(fieldState: FieldState, suggestions: List<String>) {
    AnimatedVisibility(visible = fieldState.expanded) {
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .width(fieldState.textFieldSize.width.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                val filtered = suggestions.filter {
                    it.contains(fieldState.textFieldValue.text.trim(), ignoreCase = true)
                }
                items(filtered.sorted()) { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                fieldState.textFieldValue = TextFieldValue(
                                    text = suggestion,
                                    selection = TextRange(suggestion.length)
                                )
                                fieldState.expanded = false
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}