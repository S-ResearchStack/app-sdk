package researchstack.wearable.standalone.presentation.component

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

private val TAG = "GraphAnimationView"

@Composable
fun GraphAnimationView(graphAnimation: GraphAnimation, color: Color, distant: Float = 4f) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .drawWithCache {
                fun calculateY(value: Int): Float {
                    return size.height / 2 + value
                }

                val path = Path()
                graphAnimation.graph.let {
                    kotlin
                        .runCatching {
                            if (!it.isEmpty()) {
                                path.moveTo(0f, calculateY(it.first()))
                                var x = 0f
                                it.forEach { value ->
                                    path.lineTo(x, calculateY(value))
                                    x += distant
                                }
                            }
                        }
                        .onFailure {
                            Log.e(TAG, "Error while drawing graph", it)
                        }
                }
                onDrawBehind {
                    drawPath(path, color, style = Stroke(width = 5f))
                }
            }
    )
}

class GraphAnimation(val graph: ArrayDeque<Int>, private val graphSize: Int = 150) {

    private fun getValue(): Int =
        sin(System.currentTimeMillis().toDouble()).times(Random.nextInt(100)).toInt()

    fun appendRandomData() {
        if (graph.size > graphSize) {
            graph.removeFirst()
        }
        graph.addLast(getValue())
    }

    fun appendData(value: Int) {
        if (graph.size > graphSize) {
            graph.removeFirst()
        }
        graph.addLast(value)
    }

    fun copy() = GraphAnimation(graph)
}
