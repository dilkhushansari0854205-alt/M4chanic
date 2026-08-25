package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

val M4DarkNavyBg = Color(0xFF060B14)
val M4CardBg = Color(0xFF091322)
val M4CardBorder = Color(0xFF16253D)
val M4InputBg = Color(0xFF0C192C)
val M4InputBorder = Color(0xFF1B2D4B)
val M4ElectricBlue = Color(0xFF0066FF)
val M4NeonCyan = Color(0xFF00D2FF)
val M4OrangeStart = Color(0xFFFF6200)
val M4OrangeEnd = Color(0xFFFFA000)
val M4SuccessGreen = Color(0xFF00C853)
val M4SosRed = Color(0xFFFF3B30)

/**
 * High-polish brand logo with blue location pin containing a white wrench and text "M4chanic"
 */
@Composable
fun M4chanicLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    fontSize: Int = 18,
    showText: Boolean = true,
    textColor: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.size(iconSize)) {
            val w = size.width
            val h = size.height

            val pinPath = Path().apply {
                moveTo(w * 0.5f, h * 0.96f)
                cubicTo(w * 0.45f, h * 0.85f, w * 0.12f, h * 0.60f, w * 0.12f, h * 0.38f)
                cubicTo(w * 0.12f, h * 0.17f, w * 0.29f, h * 0.04f, w * 0.5f, h * 0.04f)
                cubicTo(w * 0.71f, h * 0.04f, w * 0.88f, h * 0.17f, w * 0.88f, h * 0.38f)
                cubicTo(w * 0.88f, h * 0.60f, w * 0.55f, h * 0.85f, w * 0.5f, h * 0.96f)
                close()
            }

            drawPath(
                path = pinPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2979FF), Color(0xFF0052CC))
                )
            )

            drawCircle(
                color = Color.White,
                radius = w * 0.24f,
                center = Offset(w * 0.5f, h * 0.38f)
            )

            val wrenchPath = Path().apply {
                val cx = w * 0.5f
                val cy = h * 0.38f
                val r = w * 0.12f

                moveTo(cx - r * 0.7f, cy + r * 0.7f)
                lineTo(cx + r * 0.2f, cy - r * 0.2f)
                lineTo(cx + r * 0.6f, cy - r * 0.1f)
                lineTo(cx + r * 0.8f, cy - r * 0.7f)
                lineTo(cx + r * 0.3f, cy - r * 0.6f)
                lineTo(cx + r * 0.1f, cy - r * 0.3f)
                lineTo(cx - r * 0.4f, cy + r * 0.2f)
                close()
            }

            drawPath(path = wrenchPath, color = Color(0xFF0052CC), style = Fill)
        }

        if (showText) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "M4chanic",
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 0.2.sp
            )
        }
    }
}

/**
 * Revolving glowing border card - smoothly travels around all 4 edges of the container in 360 degrees
 */
@Composable
fun GlowingCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "revolvingGlow")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(M4CardBg)
            .border(
                width = 1.dp,
                color = M4CardBorder,
                shape = RoundedCornerShape(cornerRadius)
            )
            .drawBehind {
                val r = cornerRadius.toPx()
                val strokeW = 3.dp.toPx()
                val perimeterPath = Path().apply {
                    addRoundRect(
                        androidx.compose.ui.geometry.RoundRect(
                            0f, 0f, size.width, size.height,
                            CornerRadius(r, r)
                        )
                    )
                }

                val pathMeasure = PathMeasure()
                pathMeasure.setPath(perimeterPath, false)
                val totalLength = pathMeasure.length

                val beamLength = totalLength * 0.28f
                val startDistance = (totalLength * progress) % totalLength
                val endDistance = (startDistance + beamLength)

                val beamPath = Path()
                if (endDistance <= totalLength) {
                    pathMeasure.getSegment(startDistance, endDistance, beamPath, true)
                } else {
                    pathMeasure.getSegment(startDistance, totalLength, beamPath, true)
                    pathMeasure.getSegment(0f, endDistance - totalLength, beamPath, true)
                }

                // 1. Soft Outer Ambient Glow
                drawPath(
                    path = beamPath,
                    color = M4NeonCyan.copy(alpha = 0.35f),
                    style = Stroke(width = strokeW * 2.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 2. Focused Neon Beam
                drawPath(
                    path = beamPath,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            M4NeonCyan,
                            M4ElectricBlue,
                            Color(0xFF80E5FF),
                            M4NeonCyan
                        ),
                        center = Offset(size.width / 2f, size.height / 2f)
                    ),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Second complementary orbiting pulse opposite side
                val beamLength2 = totalLength * 0.18f
                val startDistance2 = ((totalLength * progress) + totalLength * 0.5f) % totalLength
                val endDistance2 = (startDistance2 + beamLength2)
                val beamPath2 = Path()
                if (endDistance2 <= totalLength) {
                    pathMeasure.getSegment(startDistance2, endDistance2, beamPath2, true)
                } else {
                    pathMeasure.getSegment(startDistance2, totalLength, beamPath2, true)
                    pathMeasure.getSegment(0f, endDistance2 - totalLength, beamPath2, true)
                }

                drawPath(
                    path = beamPath2,
                    color = M4ElectricBlue.copy(alpha = 0.6f),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
            .padding(22.dp)
    ) {
        content()
    }
}

/**
 * Outline blueprint art of Tow Truck carrying car + subtle city line
 */
@Composable
fun TowTruckIllustration(
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF1E3A5F)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawLine(
            color = lineColor.copy(alpha = 0.7f),
            start = Offset(0f, h * 0.82f),
            end = Offset(w, h * 0.82f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        val cityPath = Path().apply {
            moveTo(w * 0.05f, h * 0.82f)
            lineTo(w * 0.05f, h * 0.45f)
            lineTo(w * 0.15f, h * 0.45f)
            lineTo(w * 0.15f, h * 0.55f)
            lineTo(w * 0.28f, h * 0.55f)
            lineTo(w * 0.28f, h * 0.38f)
            lineTo(w * 0.42f, h * 0.38f)
            lineTo(w * 0.42f, h * 0.60f)
            lineTo(w * 0.55f, h * 0.60f)
            lineTo(w * 0.55f, h * 0.48f)
            lineTo(w * 0.70f, h * 0.48f)
            lineTo(w * 0.70f, h * 0.82f)
        }
        drawPath(
            path = cityPath,
            color = lineColor.copy(alpha = 0.25f),
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            )
        )

        val truckPath = Path().apply {
            moveTo(w * 0.10f, h * 0.68f)
            lineTo(w * 0.62f, h * 0.68f)
            lineTo(w * 0.62f, h * 0.48f)
            lineTo(w * 0.78f, h * 0.48f)
            lineTo(w * 0.88f, h * 0.62f)
            lineTo(w * 0.88f, h * 0.76f)
            lineTo(w * 0.78f, h * 0.76f)
            lineTo(w * 0.22f, h * 0.76f)
            lineTo(w * 0.10f, h * 0.74f)
            close()
        }
        drawPath(
            path = truckPath,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val windowPath = Path().apply {
            moveTo(w * 0.66f, h * 0.52f)
            lineTo(w * 0.76f, h * 0.52f)
            lineTo(w * 0.84f, h * 0.62f)
            lineTo(w * 0.66f, h * 0.62f)
            close()
        }
        drawPath(path = windowPath, color = lineColor, style = Stroke(width = 1.5.dp.toPx()))

        drawCircle(
            color = lineColor,
            radius = w * 0.055f,
            center = Offset(w * 0.22f, h * 0.78f),
            style = Stroke(width = 2.5.dp.toPx())
        )
        drawCircle(
            color = lineColor,
            radius = w * 0.055f,
            center = Offset(w * 0.76f, h * 0.78f),
            style = Stroke(width = 2.5.dp.toPx())
        )

        val carPath = Path().apply {
            moveTo(w * 0.14f, h * 0.66f)
            lineTo(w * 0.18f, h * 0.58f)
            lineTo(w * 0.26f, h * 0.54f)
            lineTo(w * 0.38f, h * 0.54f)
            lineTo(w * 0.48f, h * 0.58f)
            lineTo(w * 0.55f, h * 0.64f)
            lineTo(w * 0.55f, h * 0.66f)
            close()
        }
        drawPath(
            path = carPath,
            color = lineColor.copy(alpha = 0.9f),
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        drawCircle(
            color = lineColor,
            radius = w * 0.035f,
            center = Offset(w * 0.22f, h * 0.66f),
            style = Stroke(width = 2.dp.toPx())
        )
        drawCircle(
            color = lineColor,
            radius = w * 0.035f,
            center = Offset(w * 0.46f, h * 0.66f),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * WhatsApp Vector Icon
 */
@Composable
fun WhatsAppIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        drawCircle(
            color = Color(0xFF25D366),
            radius = w * 0.48f,
            center = Offset(w * 0.5f, h * 0.5f)
        )

        val path = Path().apply {
            moveTo(w * 0.35f, h * 0.40f)
            cubicTo(w * 0.38f, h * 0.32f, w * 0.45f, h * 0.34f, w * 0.47f, h * 0.38f)
            lineTo(w * 0.50f, h * 0.45f)
            cubicTo(w * 0.52f, h * 0.48f, w * 0.49f, h * 0.52f, w * 0.47f, h * 0.54f)
            cubicTo(w * 0.50f, h * 0.60f, w * 0.54f, h * 0.64f, w * 0.60f, h * 0.67f)
            cubicTo(w * 0.62f, h * 0.65f, w * 0.66f, h * 0.62f, w * 0.69f, h * 0.64f)
            lineTo(w * 0.76f, h * 0.67f)
            cubicTo(w * 0.80f, h * 0.69f, w * 0.82f, h * 0.76f, w * 0.74f, h * 0.79f)
            cubicTo(w * 0.65f, h * 0.82f, w * 0.48f, h * 0.72f, w * 0.38f, h * 0.62f)
            cubicTo(w * 0.28f, h * 0.52f, w * 0.28f, h * 0.44f, w * 0.35f, h * 0.40f)
            close()
        }
        drawPath(path = path, color = Color.White, style = Fill)
    }
}

/**
 * Safe & Secure Shield Icon
 */
@Composable
fun ShieldIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color(0xFF64748B)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.1f)
            lineTo(w * 0.85f, h * 0.25f)
            cubicTo(w * 0.85f, h * 0.65f, w * 0.5f, h * 0.9f, w * 0.5f, h * 0.9f)
            cubicTo(w * 0.5f, h * 0.9f, w * 0.15f, h * 0.65f, w * 0.15f, h * 0.25f)
            close()
        }

        drawPath(
            path = path,
            color = tint,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val check = Path().apply {
            moveTo(w * 0.35f, h * 0.50f)
            lineTo(w * 0.46f, h * 0.62f)
            lineTo(w * 0.68f, h * 0.40f)
        }
        drawPath(
            path = check,
            color = tint,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

/**
 * Modern Dark Bottom Navigation Bar matching the 4 primary customer app tabs:
 * [Home, Orders, Chat/Notifications, Profile]
 */
@Composable
fun CustomerBottomNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = M4CardBg,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = M4CardBorder,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                label = "Home",
                icon = if (selectedTab == "home") Icons.Filled.Home else Icons.Outlined.Home,
                isSelected = selectedTab == "home",
                onClick = { onTabSelected("home") }
            )
            NavBarItem(
                label = "Orders",
                icon = if (selectedTab == "orders") Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                isSelected = selectedTab == "orders",
                onClick = { onTabSelected("orders") }
            )
            NavBarItem(
                label = "Chat",
                icon = Icons.Outlined.ChatBubbleOutline,
                isSelected = selectedTab == "chat",
                onClick = { onTabSelected("chat") }
            )
            NavBarItem(
                label = "Profile",
                icon = if (selectedTab == "profile") Icons.Filled.Person else Icons.Outlined.Person,
                isSelected = selectedTab == "profile",
                onClick = { onTabSelected("profile") }
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) M4ElectricBlue else Color(0xFF64748B),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) M4ElectricBlue else Color(0xFF64748B)
        )
    }
}
