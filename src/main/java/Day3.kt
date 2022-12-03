fun main() {
    val lines = object {}.javaClass.getResourceAsStream("day3.txt")?.bufferedReader()?.readLines()!!

    println(task1(lines))
    println(task2(lines))
}

private fun task1(input: List<String>) = input
    .map { it.take(it.length / 2) to it.drop(it.length / 2) }
    .map { it.first.toSet() to it.second.toSet() }
    .flatMap { it.first intersect it.second }
    .sumOf { it.toValue() }

private fun task2(input: List<String>) = input
    .chunked(3)
    .flatMap { it[0].toSet() intersect it[1].toSet() intersect it[2].toSet() }
    .sumOf { it.toValue() }

private fun Char.toValue() = if (this.isLowerCase()) this - 'a' + 1 else this - 'A' + 27
