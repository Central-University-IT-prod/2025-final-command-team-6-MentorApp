package com.prodmobile.template.feature.auth.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prodmobile.template.core.components.NetworkImage
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationEvent
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationState

@Composable
fun MentorNameAndImagePage(
    state: MentorRegistrationState,
    onEvent: (MentorRegistrationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val fullNameText = state.name
    val descriptionText = state.about
    val selectedImageUri = state.image

    var signInInProcess by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onEvent(MentorRegistrationEvent.AvatarUpdate(uri, context.contentResolver))
            }
        }
    )

    if (signInInProcess)
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Добавь своё фото",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 32.dp)
                )

                Spacer(Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .size(150.dp)
                        .clickable {
                            imagePicker.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri == null) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White
                        )
                    } else {
                        NetworkImage(
                            forceLoading = state.isImageLoading,
                            image = selectedImageUri,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Своё полное имя",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 32.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                placeholder = {
                    Text(
                        text = "Василий Опытный",
                        color = Color.White.copy(0.2f)
                    )
                },
                maxLines = 1,
                value = fullNameText,
                onValueChange = {
                    onEvent(MentorRegistrationEvent.NameUpdate(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "О себе",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 32.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                placeholder = {
                    Text(
                        text = "Сорок лет как...",
                        color = Color.White.copy(0.2f)
                    )
                },
                value = descriptionText ?: "",
                onValueChange = {
                    onEvent(MentorRegistrationEvent.AboutUpdate(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = fullNameText.isNotBlank() && !state.isImageLoading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = {
                                onEvent(MentorRegistrationEvent.SignIn)
                                signInInProcess = true
                            })
                            .padding(4.dp)
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp
                        )
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Text(
                            text = "Продолжить",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}