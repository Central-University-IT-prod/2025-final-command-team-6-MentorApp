package com.prodmobile.template.feature.account.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prodmobile.template.R
import com.prodmobile.template.core.components.NetworkImage
import com.prodmobile.template.feature.account.view_model.mentor.MentorInfoState
import com.prodmobile.template.feature.account.view_model.mentor.OwnMentorInfoEvent

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OwnMentorProfileScreen(
    state: MentorInfoState?,
    onEvent: (OwnMentorInfoEvent) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val snackBar = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(0) }
    val bottomBarHeight = 80.dp

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBar,
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding())
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                contentAlignment = Alignment.Center
            ) {
                ElevatedButton(
                    onClick = { onEvent(OwnMentorInfoEvent.SignOut) },
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Выйти из аккаунта",
                        color = Color.White
                    )
                }
            }
        }
    ) {
        if (state == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF000000),
                                        Color(0x8B000000),
                                        Color(0xFF000000),
                                    )
                                )
                            )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.math_background),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer { alpha = 0.09f }
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .statusBarsPadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(3.dp, Color.White, CircleShape)
                            ) {
                                NetworkImage(
                                    imageUrl = state.avatarUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(Modifier.height(32.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = state.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 1f),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "МЕНТОР",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Мои навыки:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(
                                    top = 16.dp,
                                    bottom = 8.dp
                                )
                        )

                        FlowRow(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            state.skills.forEach {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(
                                        alpha = 1f
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = it,
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Контакты:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                        )

                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.contacts.forEach { contact ->
                                Text(
                                    text = "${contact.messenger}: ${contact.id}",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                item {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("О себе") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text("Отзывы ${if (state.reviews.isNotEmpty()) "(${state.reviews.size})" else ""}")
                            }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = state.mentorDescription ?: "Пока нет описания",
                                fontSize = 16.sp
                            )
                        }
                    }

                    1 -> {
                        if (state.reviews.isNotEmpty()) {
                            itemsIndexed(state.reviews) { index, review ->
                                ReviewItem(
                                    reviewInfo = review,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                if (index < state.reviews.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        } else {
                            item {
                                Text(
                                    text = "Пока отзывов нет",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Добавляем отступ для нижней кнопки
                item {
                    Spacer(modifier = Modifier.height(bottomBarHeight))
                }
            }
        }
    }
}