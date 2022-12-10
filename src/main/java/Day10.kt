import models.Point
import models.printArea

fun main() {
    val input = object {}.javaClass.getResourceAsStream("day10.txt")?.bufferedReader()?.readLines()!!
    println(solve1(input))
    println(solve2(input))
}

private fun solve1(lines: List<String>): Int {
    var x = 1
    var cycle = 1
    var sum = 0

    fun processCycle() {
        if (cycle % 40 == 20) sum += cycle * x
        cycle++
    }

    lines.forEach { line ->
        if (line == "noop") processCycle()
        else {
            processCycle(); processCycle()
            x += line.substringAfter(" ").toInt()
        }
    }
    return sum
}

private fun solve2(lines: List<String>) {
    var x = 1
    var cycle = 1
    val screen = mutableMapOf<Point, Boolean>()

    fun processCycle() {
        val screenX = (cycle - 1) % 40
        if (screenX in (x - 1)..(x + 1)) {
            screen[Point(screenX, (cycle - 1) / 40)] = true
        }
        cycle++
    }

    lines.forEach { line ->
        if (line == "noop") processCycle()
        else {
            processCycle(); processCycle()
            x += line.substringAfter(" ").toInt()
        }
    }
    screen.printArea()
}
