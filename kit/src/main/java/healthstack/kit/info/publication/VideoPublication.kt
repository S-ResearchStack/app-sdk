package healthstack.kit.info.publication

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import healthstack.kit.R
import healthstack.kit.info.publication.content.ContentBlock
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.concurrent.TimeUnit

class VideoPublication(
    coverContent: String?,
    category: String,
    title: String,
    description: String,
    contents: List<ContentBlock>,
) : Publication(coverContent, category, title, description, contents) {
    @Composable
    override fun Render(
        onClick: (Publication?) -> Unit
    ) {
        val scrollState = rememberScrollState()
        var lifecycle by remember {
            mutableStateOf(Lifecycle.Event.ON_CREATE)
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Scaffold(
            topBar = {
                TopBar("Education") {
                    onClick(null)
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                VideoPlayer(
                    uri = Uri
                        .parse(coverContent),
                    lifecycle = lifecycle
                )
                Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = category,
                            color = AppTheme.colors.onBackground.copy(0.6f),
                            textAlign = TextAlign.Left,
                            style = AppTheme.typography.body3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = title,
                            color = AppTheme.colors.onBackground,
                            textAlign = TextAlign.Left,
                            style = AppTheme.typography.headline3
                        )
                        for (block in contents) {
                            Spacer(modifier = Modifier.height(24.dp))
                            block.Render()
                        }
                        this@VideoPublication.RelatedContent()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    @Composable
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun VideoPlayer(
        modifier: Modifier = Modifier,
        uri: Uri,
        lifecycle: Lifecycle.Event
    ) {
        val context = LocalContext.current
        val player = remember {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                        context,
                        DefaultDataSource.Factory(context)
                    )
                    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri))
                    setMediaSource(mediaSource)
                    setHandleAudioBecomingNoisy(true)
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    repeatMode = Player.REPEAT_MODE_OFF
                    prepare()
                }
        }

        var controlsVisible by remember { mutableStateOf(true) }
        var isPlaying by remember { mutableStateOf(player.isPlaying) }
        var isMute by remember { mutableStateOf(false) }
        var isReady by remember { mutableStateOf(false) }
        var totalDuration by remember { mutableStateOf(0L) }
        var currentTime by remember { mutableStateOf(0L) }
        var bufferedPercentage by remember { mutableStateOf(0) }
        var playbackState by remember { mutableStateOf(player.playbackState) }
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        ) {
            DisposableEffect(
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            setPlayer(player)
                            useController = false
                        }
                    },
                    update = {
                        when (lifecycle) {
                            Lifecycle.Event.ON_PAUSE -> {
                                it.onPause()
                                it.player?.pause()
                            }
                            Lifecycle.Event.ON_RESUME -> {
                                it.onResume()
                            }
                            else -> {}
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            controlsVisible = controlsVisible.not()
                        }
                )
            ) {
                val listener =
                    object : Player.Listener {
                        override fun onEvents(player: Player, events: Player.Events) {
                            super.onEvents(player, events)
                            if (!isReady && player.playbackState == Player.STATE_READY) {
                                totalDuration = player.duration
                                isReady = true
                            }
                            currentTime = player.currentPosition
                            bufferedPercentage = player.bufferedPercentage
                            isPlaying = player.isPlaying
                            playbackState = player.playbackState
                        }
                    }
                player.addListener(listener)

                onDispose {
                    player.removeListener(listener)
                    player.release()
                }
            }
            LaunchedEffect(key1 = player, key2 = isPlaying) {
                while (isActive && isPlaying) {
                    currentTime = player.currentPosition
                    bufferedPercentage = player.bufferedPercentage
                    delay(100)
                }
            }
            LaunchedEffect(key1 = controlsVisible, key2 = isPlaying) {
                if (controlsVisible && isPlaying) {
                    delay(3000)
                    controlsVisible = false
                }
            }
            CustomControls(
                isVisible = { controlsVisible },
                isPlaying = { isPlaying },
                onPauseToggle = {
                    when {
                        player.isPlaying -> {
                            player.pause()
                        }
                        player.isPlaying.not() && playbackState == STATE_ENDED -> {
                            player.seekTo(0)
                        }
                        else -> {
                            player.play()
                        }
                    }
                },
                isMute = { isMute },
                onMuteToggle = {
                    isMute = isMute.not()
                    player.volume = if (isMute) 0f else 1f
                },
                totalDuration = { totalDuration },
                currentTime = { currentTime },
                bufferedPercentage = { bufferedPercentage },
                onSeekChanged = { timeMs: Float ->
                    player.seekTo(timeMs.toLong())
                }
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun CustomControls(
        modifier: Modifier = Modifier,
        isVisible: () -> Boolean,
        isPlaying: () -> Boolean,
        onPauseToggle: () -> Unit,
        isMute: () -> Boolean,
        onMuteToggle: () -> Unit,
        totalDuration: () -> Long,
        currentTime: () -> Long,
        bufferedPercentage: () -> Int,
        onSeekChanged: (timeMs: Float) -> Unit,
    ) {
        val visible = remember(isVisible()) {
            isVisible()
        }

        AnimatedVisibility(
            modifier = modifier
                .fillMaxSize()
                .testTag("Custom Video Controls"),
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .background(AppTheme.colors.onBackground.copy(0.4f))
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        modifier = Modifier.size(56.dp),
                        onClick = onPauseToggle
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter =
                            when {
                                isPlaying() -> {
                                    painterResource(id = R.drawable.ic_pause)
                                }
                                else -> {
                                    painterResource(id = R.drawable.ic_play)
                                }
                            },
                            contentDescription = "Play/Pause"
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    AppTheme.colors.onBackground.copy(0.8f)
                                )
                            )
                        )
                        .animateEnterExit(
                            enter = slideInVertically { fullHeight: Int -> fullHeight },
                            exit = slideOutVertically { fullHeight: Int -> fullHeight }
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Slider(
                            value = bufferedPercentage().toFloat(),
                            onValueChange = {},
                            enabled = false,
                            valueRange = 0f..100f,
                            colors =
                            SliderDefaults.colors(
                                disabledThumbColor = Color.Transparent,
                                disabledActiveTrackColor = AppTheme.colors.onPrimary.copy(0.3f)
                            )
                        )
                        Slider(
                            value = currentTime().toFloat(),
                            onValueChange = onSeekChanged,
                            valueRange = 0f..totalDuration().toFloat(),
                            colors =
                            SliderDefaults.colors(
                                thumbColor = AppTheme.colors.onPrimary,
                                activeTrackColor = AppTheme.colors.onPrimary,
                                inactiveTrackColor = AppTheme.colors.onPrimary.copy(0.1f)
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = totalDuration().formatTime(),
                                modifier = Modifier.align(Alignment.CenterVertically),
                                color = AppTheme.colors.onPrimary,
                                style = AppTheme.typography.body3
                            )
                            IconButton(
                                modifier = Modifier.size(20.dp),
                                onClick = onMuteToggle
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter =
                                    when {
                                        isMute() -> {
                                            painterResource(id = R.drawable.ic_volume_off)
                                        }
                                        else -> {
                                            painterResource(id = R.drawable.ic_volume_on)
                                        }
                                    },
                                    contentDescription = "Mute/Unmute"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun PreviewImage() {
        val retriever by remember { mutableStateOf(MediaMetadataRetriever()) }
        var sourceSet by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {
            sourceSet = try {
                retriever.setDataSource(coverContent, HashMap<String, String>())
                true
            } catch (e: Exception) {
                false
            }
            onDispose {
                retriever.release()
            }
        }

        if (sourceSet) {
            val thumbnail = retriever.frameAtTime!!.asImageBitmap()
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
            Box(
                modifier = Modifier.height(98.dp).width(116.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = thumbnail,
                    contentDescription = "Video Thumbnail",
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .height(24.dp)
                        .padding(start = 4.dp, bottom = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AppTheme.colors.onBackground.copy(0.6f)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_preview_play),
                        contentDescription = "Duration Preview",
                        modifier = Modifier.padding(start = 6.dp, end = 2.dp).size(8.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = duration.formatTime(),
                        modifier = Modifier.padding(end = 6.dp),
                        color = AppTheme.colors.onPrimary,
                        style = AppTheme.typography.overline1
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(R.drawable.ic_education_default),
                contentDescription = "Default Image",
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )
        }
    }

    private fun Long.formatTime(): String {
        return if (TimeUnit.MILLISECONDS.toHours(this) > 0) {
            String.format(
                "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(this),
                TimeUnit.MILLISECONDS.toMinutes(this) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)),
                TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
            )
        } else {
            String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(this),
                TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
            )
        }
    }
}
