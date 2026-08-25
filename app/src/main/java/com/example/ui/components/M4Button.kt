package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActionOrange
import com.example.ui.theme.PrimaryBlue

enum class M4ButtonStyle {
    PRIMARY,
    BLUE,
    ORANGE,
    OUTLINE,
    SECONDARY,
    WHITE
}

@Composable
fun M4Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: M4ButtonStyle = M4ButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    val height = 54.dp
    val shape = RoundedCornerShape(14.dp)

    when (style) {
        M4ButtonStyle.OUTLINE, M4ButtonStyle.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled && !isLoading,
                shape = shape,
                border = BorderStroke(1.5.dp, PrimaryBlue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryBlue
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, PrimaryBlue)
            }
        }
        M4ButtonStyle.ORANGE -> {
            Button(
                onClick = onClick,
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ActionOrange,
                    contentColor = Color.White,
                    disabledContainerColor = ActionOrange.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, Color.White)
            }
        }
        M4ButtonStyle.WHITE -> {
            Button(
                onClick = onClick,
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = PrimaryBlue
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, PrimaryBlue)
            }
        }
        M4ButtonStyle.PRIMARY, M4ButtonStyle.BLUE -> {
            Button(
                onClick = onClick,
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White,
                    disabledContainerColor = PrimaryBlue.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                ButtonContent(text, isLoading, leadingIcon, trailingIcon, Color.White)
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    isLoading: Boolean,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    contentColor: Color
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = contentColor,
            strokeWidth = 2.5.dp
        )
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                androidx.compose.material3.Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                androidx.compose.material3.Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor
                )
            }
        }
    }
}
