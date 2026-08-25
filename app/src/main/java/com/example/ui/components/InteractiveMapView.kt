package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.LocationPoint
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.PrimaryBlue

@Composable
fun MechanicActiveJobMap(
    customerLoc: LocationPoint,
    mechanicLoc: LocationPoint?,
    customerName: String,
    serviceType: String,
    distanceKm: Double,
    etaMinutes: Int,
    modifier: Modifier = Modifier,
    onNavigateClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "map_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Dark tactical map canvas
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Dark base background
            drawRect(color = Color(0xFF0F172A), size = size)

            // 2. City Blocks (Dark Blue-Grey)
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(width * 0.08f, height * 0.12f),
                size = androidx.compose.ui.geometry.Size(width * 0.38f, height * 0.22f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )

            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(width * 0.55f, height * 0.58f),
                size = androidx.compose.ui.geometry.Size(width * 0.38f, height * 0.25f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )

            // 3. Roads & Avenues (Slate grey grid)
            val roadColor = Color(0xFF334155)

            // Diagonal expressway
            drawLine(
                color = roadColor,
                start = Offset(0f, height * 0.32f),
                end = Offset(width, height * 0.42f),
                strokeWidth = 24f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = roadColor,
                start = Offset(width * 0.28f, 0f),
                end = Offset(width * 0.32f, height),
                strokeWidth = 20f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = roadColor,
                start = Offset(width * 0.72f, 0f),
                end = Offset(width * 0.68f, height),
                strokeWidth = 18f,
                cap = StrokeCap.Round
            )

            // 4. GPS Polyline from Mechanic to Customer
            val startMechanic = Offset(width * 0.32f, height * 0.68f)
            val endCustomer = Offset(width * 0.72f, height * 0.28f)

            val routePath = Path().apply {
                moveTo(startMechanic.x, startMechanic.y)
                lineTo(width * 0.32f, height * 0.38f)
                lineTo(width * 0.72f, height * 0.38f)
                lineTo(endCustomer.x, endCustomer.y)
            }

            // Glow / casing
            drawPath(
                routePath,
                color = PrimaryBlue.copy(alpha = 0.3f),
                style = Stroke(width = 14f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Primary Blue Route
            drawPath(
                routePath,
                color = PrimaryBlue,
                style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        // Mechanic Marker (You) - Green Pulsing Marker with Vehicle Icon
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 120.dp, start = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            // Pulse circle
            Box(
                modifier = Modifier
                    .size((48 * pulseScale).dp)
                    .clip(CircleShape)
                    .background(OnlineGreen.copy(alpha = 0.25f))
            )
            Surface(
                shape = CircleShape,
                color = OnlineGreen,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(44.dp)
                    .border(3.dp, Color.White, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Moped,
                        contentDescription = "You (Mechanic)",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Customer Location Pin - Orange breakdown pin with customer label
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 90.dp, end = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ActionOrange,
                    shadowElevation = 8.dp,
                    modifier = Modifier.border(2.dp, Color.White, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = customerName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = ActionOrange,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Distance & ETA Floating Chip
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E293B),
            shadowElevation = 6.dp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Navigation,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$distanceKm km",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = " • ",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8)
                )
                Text(
                    text = "$etaMinutes mins away",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = OnlineGreen
                )
            }
        }

        // Action Floating Buttons (Navigation & Recenter)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Open in Google Maps Button
            FloatingActionButton(
                onClick = {
                    if (onNavigateClick != null) {
                        onNavigateClick()
                    } else {
                        launchGoogleNavigation(context, customerLoc.lat, customerLoc.lng)
                    }
                },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Directions,
                    contentDescription = "Start Navigation",
                    modifier = Modifier.size(26.dp)
                )
            }

            // Recenter
            FloatingActionButton(
                onClick = { /* Recenter */ },
                containerColor = Color(0xFF1E293B),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(46.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Recenter",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun launchGoogleNavigation(context: Context, lat: Double, lng: Double) {
    try {
        val uri = Uri.parse("google.navigation:q=$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lng")
            context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
        }
    } catch (_: Exception) {
        val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lng")
        context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
    }
}

fun launchDialer(context: Context, phoneNumber: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    } catch (_: Exception) {}
}
