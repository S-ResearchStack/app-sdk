package com.samsung.healthcare.kit.view.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samsung.healthcare.kit.R.drawable
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun StatusCard(@DrawableRes drawableId: Int, value: String, unit: String = "") {
    val shape = RoundedCornerShape(4.dp)
    Card(
        shape = shape,
        backgroundColor = AppTheme.colors.surface,
        modifier = Modifier
            .wrapContentSize()
            .shadow(elevation = 2.dp, shape = shape, clip = false)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .height(150.dp)
        ) {
            Image(
                painter = painterResource(drawableId),
                contentDescription = "",
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp),
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = AppTheme.typography.subHeader2,
                color = AppTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                maxLines = 1
            )
            Text(
                text = unit,
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textHint,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusCardPreview() =
    Box {
        StatusCard(drawable.ic_heart, "87", "BPM")
    }

@Preview(showBackground = true)
@Composable
fun TaskStatusCardPreview() =
    Box {
        StatusCard(drawable.ic_task, "2", "Remaining")
    }
