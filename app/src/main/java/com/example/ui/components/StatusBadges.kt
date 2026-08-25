package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedLight
import com.example.ui.theme.OnlineGreen
import com.example.ui.theme.OnlineGreenLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.WarningYellow
import com.example.ui.theme.WarningYellowLight

@Composable
fun OnlineBadge(
    isOnline: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(if (isOnline) OnlineGreenLight else Color(0xFF334155), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(if (isOnline) OnlineGreen else Color(0xFF94A3B8), CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isOnline) "ONLINE" else "OFFLINE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isOnline) OnlineGreen else Color(0xFF94A3B8)
        )
    }
}

@Composable
fun MechanicApprovalBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon, label) = when (status.lowercase()) {
        "approved" -> Quadruple(OnlineGreenLight, OnlineGreen, Icons.Filled.CheckCircle, "Approved Partner")
        "pending" -> Quadruple(WarningYellowLight, Color(0xFFB45309), Icons.Filled.HourglassEmpty, "Under Review")
        "rejected" -> Quadruple(ErrorRedLight, ErrorRed, Icons.Filled.Error, "Application Rejected")
        "suspended" -> Quadruple(ErrorRedLight, ErrorRed, Icons.Filled.Error, "Account Suspended")
        else -> Quadruple(WarningYellowLight, Color(0xFFB45309), Icons.Filled.HourglassEmpty, "Pending")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun OrderStatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        OrderStatus.PENDING -> Triple(WarningYellowLight, Color(0xFFB45309), "New Request")
        OrderStatus.ACCEPTED -> Triple(Color(0xFFFFEAD5), ActionOrange, "Active Job")
        OrderStatus.ON_THE_WAY -> Triple(Color(0xFFFFEAD5), ActionOrange, "On The Way")
        OrderStatus.ARRIVING_SOON -> Triple(Color(0xFFE0F2FE), PrimaryBlue, "Arriving Soon")
        OrderStatus.ARRIVED -> Triple(Color(0xFFEDE9FE), Color(0xFF7C3AED), "Arrived")
        OrderStatus.IN_PROGRESS -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Repairing")
        OrderStatus.COMPLETED -> Triple(OnlineGreenLight, OnlineGreen, "Completed")
        OrderStatus.REJECTED -> Triple(ErrorRedLight, ErrorRed, "Rejected / Cancelled")
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun StatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    OrderStatusBadge(status = status, modifier = modifier)
}

@Composable
fun MechanicOnlineToggleSwitch(
    isOnline: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val trackBgColor by animateColorAsState(
        targetValue = if (isOnline) OnlineGreen else Color(0xFF475569),
        animationSpec = tween(300),
        label = "switchTrackColor"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (isOnline) 32.dp else 4.dp,
        animationSpec = tween(300),
        label = "switchThumbOffset"
    )

    Box(
        modifier = modifier
            .width(68.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(trackBgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = !isLoading
            ) {
                onToggle(!isOnline)
            }
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(26.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (isOnline) OnlineGreen else Color(0xFF94A3B8))
            )
        }
    }
}

@Composable
fun InteractiveRatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    starSize: Dp = 36.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val isSelected = i <= rating
            Icon(
                imageVector = if (isSelected) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "$i stars",
                tint = if (isSelected) WarningYellow else Color(0xFFCBD5E1),
                modifier = Modifier
                    .size(starSize)
                    .clickable { onRatingChange(i) }
                    .padding(4.dp)
            )
        }
    }
}
