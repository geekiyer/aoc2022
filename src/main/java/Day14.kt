import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = object {}.javaClass.getResourceAsStream("day14.txt")?.bufferedReader()?.readLines()!!
    println(Day14().solve(false, input))
}

class Day14 {
    fun solve(isPart1: Boolean, input: List<String>): String {
        var answer = 0
        val grid = Array(1000) {
            BooleanArray(
                1000
            )
        }
        var maxy = 0
        input.forEach { line ->
            maxy = max(addPath(line, grid), maxy)
        }
        if (!isPart1) {
            addPath("0," + (maxy + 2) + " -> 999," + (maxy + 2), grid)
        }
        while (sand(grid, maxy)) {
            answer++
        }
        return "" + answer
    }

    private fun addPath(line: String, grid: Array<BooleanArray>): Int {
        var ret = 0
        val coordinates = line.split(" -> ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in 1 until coordinates.size) {
            val x1 = coordinates[i - 1].toFirst()
            val y1 = coordinates[i - 1].toSecond()
            val x2 = coordinates[i].toFirst()
            val y2 = coordinates[i].toSecond()

            for (j in min(x1, x2).rangeTo(max(x1, x2))) {
                for (k in min(y1, y2).rangeTo(max(y1, y2))) {
                    grid[j][k] = true
                }
            }
            ret = max(ret, max(y1, y2))
        }
        return ret
    }

    private fun String.toFirst() = this.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
    private fun String.toSecond() = this.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

    private fun sand(grid: Array<BooleanArray>, maxy: Int): Boolean {
        if (grid[500][0]) {
            return false
        }
        var x = 500
        var y = 0
        while (y <= maxy + 3) {
            if (!grid[x][y + 1]) {
                y++
                continue
            }
            if (!grid[x - 1][y + 1]) {
                y++
                x--
                continue
            }
            if (!grid[x + 1][y + 1]) {
                y++
                x++
                continue
            }
            grid[x][y] = true
            return true
        }
        return false
    }
}