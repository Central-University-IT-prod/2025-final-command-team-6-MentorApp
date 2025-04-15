package com.prodmobile.template.feature.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prodmobile.template.core.models.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RoleSelectionScreen(
    onNavigationToStudentRegistration: () -> Unit,
    onNavigationToMentorsRegistration: () -> Unit,
) {
    val coroutine = rememberCoroutineScope()
    var selectedRole: UserRole by remember { mutableStateOf(UserRole.Unauthorized) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Привет!\nКто ты?",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(start = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        SelectableBox(
            text = "Ментор",
            isSelected = selectedRole == UserRole.Mentor,
            onSelected = {
                selectedRole = UserRole.Mentor

                coroutine.launch {
                    delay(200)
                    onNavigationToMentorsRegistration()
                }
            },
            modifier = Modifier.defaultRoleButtonModifier()
        )

        SelectableBox(
            text = "Студент",
            isSelected = selectedRole == UserRole.Student,
            onSelected = {
                selectedRole = UserRole.Student

                coroutine.launch {
                    delay(200)
                    onNavigationToStudentRegistration()
                }
            },
            modifier = Modifier.defaultRoleButtonModifier()
        )
    }

}


@Composable
fun SelectableBox(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .clickable(onClick = onSelected)
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = animatedBorderColor
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White
        )
    }
}

fun Modifier.defaultRoleButtonModifier() = this
    .height(75.dp)
    .widthIn(max = 350.dp)
    .fillMaxWidth()
    .padding(8.dp)