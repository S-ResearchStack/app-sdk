package com.samsung.healthcare.kit.view.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.theme.AppTheme

@Composable
fun SdkCard(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = AppTheme.colors.onBackground,
    contentColor: Color = AppTheme.colors.textPrimaryAccent,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(
                if (border != null) androidx.compose.ui.Modifier.border(
                    border,
                    shape
                ) else Modifier
            )
            .background(
                color = color,
                shape = shape
            )
            .clip(shape)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

@Preview(showBackground = true)
@Composable
fun SdkCardPreview() {
    SdkCard(
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        modifier = Modifier
            .size(
                width = 255.dp,
                height = 375.dp
            )
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        contentColor = AppTheme.colors.textPrimaryAccent,
        elevation = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = {})
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.card_sample_image),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Medical eligibilities",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6,
                color = AppTheme.colors.textPrimaryAccent,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• Pre-existing condition(s)",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
                color = AppTheme.colors.textHint,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
            Text(
                text = "• Prescription(s)",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
                color = AppTheme.colors.textHint,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
            Text(
                text = "• Living in the United States",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body1,
                color = AppTheme.colors.textHint,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
        }
    }
}
