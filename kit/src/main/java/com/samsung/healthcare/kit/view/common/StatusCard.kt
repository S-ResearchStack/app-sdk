package com.samsung.healthcare.kit.view.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samsung.healthcare.kit.R.drawable
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun StatusCard(@DrawableRes drawableId: Int, text: String) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.surface,
        modifier = Modifier
            .fillMaxHeight(1f)
            .width(98.dp)
            .shadow(elevation = 10.dp, shape = shape, clip = false)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(drawableId),
                    contentDescription = "",
                    modifier = Modifier
                        .width(18.dp)
                        .height(20.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                style = AppTheme.typography.body1,
                color = AppTheme.colors.textSecondary,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusCardPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
    ) {
        StatusCard(drawable.ic_sleep, "180/nBPM")
    }
}
