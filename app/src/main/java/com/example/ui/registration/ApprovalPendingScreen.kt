package com.example.ui.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.NetworkResult
import com.example.data.repository.MechanicRepository
import com.example.data.socket.SocketManager
import com.example.ui.components.M4Button
import com.example.ui.components.M4ButtonStyle
import com.example.ui.theme.WarningYellow
import kotlinx.coroutines.launch

@Composable
fun ApprovalPendingScreen(
    mechanicRepository: MechanicRepository,
    socketManager: SocketManager,
    onApproved: () -> Unit,
    onRejected: () -> Unit,
    onSuspended: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }

    // Listen to real-time socket approval event
    LaunchedEffect(Unit) {
        socketManager.statusChangeEvents.collect { newStatus ->
            if (newStatus.equals("approved", ignoreCase = true)) {
                onApproved()
            } else if (newStatus.equals("rejected", ignoreCase = true)) {
                onRejected()
            } else if (newStatus.equals("suspended", ignoreCase = true)) {
                onSuspended()
            }
        }
    }

    fun checkStatus() {
        coroutineScope.launch {
            isChecking = true
            val result = mechanicRepository.fetchMechanicProfile()
            isChecking = false
            if (result is NetworkResult.Success) {
                val profile = result.data
                when (profile.status.lowercase()) {
                    "approved" -> onApproved()
                    "rejected" -> onRejected()
                    "suspended" -> onSuspended()
                    else -> {}
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Pending Clock Graphic
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFFEF3C7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.HourglassTop,
                contentDescription = null,
                tint = WarningYellow,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Application Under Review",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Thank you for registering! Our partner verification team is reviewing your vehicle and service details. We usually approve accounts within 24 hours.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Info Card
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "What happens next?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1. Verification of service vehicle and profile.\n2. Instant push activation once approved.\n3. You will immediately start receiving breakdown calls.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        M4Button(
            text = "Check Status",
            onClick = { checkStatus() },
            leadingIcon = Icons.Filled.Refresh,
            isLoading = isChecking,
            style = M4ButtonStyle.PRIMARY
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
