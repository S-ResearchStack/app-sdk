package com.samsung.healthcare.kit.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.step.Step

open class OrderedTask(
    id: String,
    name: String,
    description: String,
    val steps: List<Step<out Model, *>>,
) : Task(
    id,
    name,
    description,
) {
    inner class Progress {
        var current: Int = 0
        val length: Int = steps.size
    }

    protected val progress: Progress = Progress()

    protected val pageableStream = MutableLiveData<Int>(0)

    protected fun hasNext(): Boolean = progress.current < progress.length - 1

    protected fun hasPrev(): Boolean = progress.current > 0

    open val pageCallbacks =
        object : CallbackCollection() {
            override fun next() {
                if (hasNext()) pageableStream.postValue(++progress.current)
                else callback?.invoke()
            }

            override fun prev() {
                if (hasPrev()) pageableStream.postValue(--progress.current)
            }
        }

    @Composable
    override fun Render() {
        val cursor: Int? by pageableStream.observeAsState()

        steps[cursor ?: 0].Render(pageCallbacks)
    }

    @Composable
    override fun CardView(onClick: () -> Unit) {
        TODO("Not yet implemented")
    }
}
