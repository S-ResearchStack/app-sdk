package healthstack.kit.task.activity.model

import healthstack.kit.task.base.StepModel

class ColorWordChallengeMeasureModel(
    id: String,
    title: String = "Color Word Challenge",
    val numTest: Int = 10,
) : StepModel(id, title, null) {
    private fun generateTestset(): Array<IntArray> {
        // +1 for buffer
        var ret = Array(numTest + 1) { IntArray(5) }
        for (i in 0 until numTest) {
            var check = BooleanArray(colorCodes.size)

            var j = 4
            while (j >= 0) {
                val cidx = (colorCodes.indices).random()
                if (!check[cidx]) {
                    check[cidx] = true
                    ret[i][j] = cidx
                    --j
                }
            }
            ret[i][0] = (1..4).random()
        }
        return ret
    }

    val colorCodes = listOf(
        0XFFFF0000,
        0xFFFF9800,
        0xFFFFEB3B,
        0xFF4CAF50,
        0xFF2196F3,
        0xFF9C27B0,
        0x99999999,
        0xFF000000,
        0xFFFFAAAA,
        0xFF854343,
    )

    val colorWords = listOf(
        "RED",
        "ORANGE",
        "YELLOW",
        "GREEN",
        "BLUE",
        "PURPLE",
        "GREY",
        "BLACK",
        "PINK",
        "BROWN",
    )

    val testset = generateTestset()

    fun getRandomColor(): Long {
        return colorCodes[(colorCodes.indices).random()]
    }

    fun getRandomWord(): String {
        return colorWords[(colorWords.indices).random()]
    }
}
