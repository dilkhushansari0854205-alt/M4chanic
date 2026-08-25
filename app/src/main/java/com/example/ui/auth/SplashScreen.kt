package com.example.ui.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.session.SessionManager
import com.example.ui.components.M4DarkNavyBg
import com.example.ui.components.M4chanicLogo
import com.example.ui.components.TowTruckIllustration
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    sessionManager: SessionManager,
    onNavigateNext: (destination: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.75f) }
    val alpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        delay(1400)

        val isLoggedIn = sessionManager.isLoggedIn()
        if (!isLoggedIn) {
            onNavigateNext("login")
        } else {
            val role = sessionManager.getRole()
            val status = sessionManager.getMechanicStatus()
            val activeOrderId = sessionManager.getActiveOrderId()

            if (!activeOrderId.isNullOrBlank()) {
                onNavigateNext("active_job_map/$activeOrderId")
            } else if (role.equals("customer", ignoreCase = true)) {
                onNavigateNext("customer_home")
            } else if (status.equals("pending", ignoreCase = true)) {
                onNavigateNext("approval_pending")
            } else if (status.equals("rejected", ignoreCase = true)) {
                onNavigateNext("registration_rejected")
            } else if (status.equals("suspended", ignoreCase = true)) {
                onNavigateNext("account_suspended")
            } else {
                onNavigateNext("dashboard")
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(M4DarkNavyBg)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp, horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.4f))

            // Center Logo & Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .scale(scale.value)
                    .alpha(alpha.value)
            ) {
                M4chanicLogo(
                    iconSize = 46.dp,
                    fontSize = 30,
                    textColor = Color.White
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Roadside help.\nRight when you need it.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // Blueprint Tow Truck Vector Illustration
            TowTruckIllustration(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .alpha(alpha.value)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Subtle Rotating Arc Spinner (Matching bottom of screen 1)
            Canvas(modifier = Modifier.size(32.dp)) {
                drawArc(
                    color = Color(0xFF0066FF),
                    startAngle = rotation,
                    sweepAngle = 100f,
                    useCenter = false,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
