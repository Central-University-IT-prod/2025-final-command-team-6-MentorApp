package com.prodmobile.template.feature.requests.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prodmobile.template.core.components.AnimatedBorder
import com.prodmobile.template.core.components.NetworkImage
import com.prodmobile.template.core.util.formatDate
import com.prodmobile.template.feature.requests.model.RequestFromStudentModel
import com.prodmobile.template.feature.requests.model.RequestStatus
import com.prodmobile.template.feature.requests.view_model.StudentsFavouritesScreenEvent
import com.prodmobile.template.feature.requests.view_model.StudentsRequestsScreenState

@Composable
fun StudentRequestsScreen(
    onNavigationToProfile: (String) -> Unit,
    state: StudentsRequestsScreenState,
    onEvent: (StudentsFavouritesScreenEvent) -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp,
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        item {
            if (state.requests.size == 0 && !state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ты еще не отправлял запросов")
                }
            } else if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        itemsIndexed(state.requests) { _, favourite ->
            RequestItem(
                request = favourite,
                onNavigationToProfile = { onNavigationToProfile(favourite.mentorId) },
                onDeny = { onEvent(StudentsFavouritesScreenEvent.DenyRequest(favourite.requestId)) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RequestItem(
    request: RequestFromStudentModel,
    onNavigationToProfile: () -> Unit,
    onDeny: () -> Unit
) {
    var clickedId by remember { mutableStateOf(request.requestId) }
    var isDenying by remember { mutableStateOf(false) }
    val formatedCreatedAt = remember { formatDate(request.createdAt) }

    AnimatedBorder(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        isActive = isDenying && clickedId == request.requestId,
        colors = listOf(
            Color(0xFFF44336),
            Color(0xFFF50057),
            Color(0xFF690909)
        ),
        durationMillis = 1500
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onNavigationToProfile()
                    }
                ) {
                    NetworkImage(
                        imageUrl = request.imageUrl,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = request.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = formatedCreatedAt,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = request.status.title.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                if (request.contacts.isNotEmpty()) {
                    Text(
                        text = "Контакты:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        request.contacts.forEach { contact ->
                            SocialLinkItem(
                                socialNetwork = contact.messenger,
                                url = contact.id
                            )
                        }
                    }
                }

                if (request.skills.isNotEmpty()) {
                    Text(
                        text = "Навыки:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        request.skills.forEach { skill ->
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 1f),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = skill,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (request.status == RequestStatus.Review) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            onClick = {
                                isDenying = true
                                clickedId = request.requestId
                                onDeny()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Отменить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialLinkItem(socialNetwork: String, url: String) {
    val clipboardManager = LocalClipboardManager.current
    val icon = Icons.Default.Link

    ElevatedAssistChip(
        onClick = {
            clipboardManager.setText(
                buildAnnotatedString {
                    append(url)
                }
            )
        },
        label = {
            SelectionContainer {
                Text(socialNetwork)
            }
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}