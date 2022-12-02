
fun main() {
    // println(part1())
    println(part2())
}

val input = object {}.javaClass.getResourceAsStream("day2.txt")?.bufferedReader()?.readLines()

private val part1Scores: Map<String, Int> =
    mapOf(
        "A X" to 1 + 3,
        "A Y" to 2 + 6,
        "A Z" to 3 + 0,
        "B X" to 1 + 0,
        "B Y" to 2 + 3,
        "B Z" to 3 + 6,
        "C X" to 1 + 6,
        "C Y" to 2 + 0,
        "C Z" to 3 + 3,
    )

private val part2Scores: Map<String, Int> =
    mapOf(
        "A X" to 3 + 0,
        "A Y" to 1 + 3,
        "A Z" to 2 + 6,
        "B X" to 1 + 0,
        "B Y" to 2 + 3,
        "B Z" to 3 + 6,
        "C X" to 2 + 0,
        "C Y" to 3 + 3,
        "C Z" to 1 + 6,
    )

fun part1(): Int =
    input!!.sumOf { part1Scores[it] ?: 0 }

fun part2(): Int =
    input!!.sumOf { part2Scores[it] ?: 0 }
