fun main() {
    println(solve(false))
    println(solve(true))
}

private val rearrangementInput = object {}.javaClass.getResourceAsStream("day5.txt")?.bufferedReader()?.readLines()!!

private fun solve(partTwo: Boolean): String {
    val crates = (1..9).associateWith { emptyList<Char>() }.toMutableMap()
    rearrangementInput.filter { '[' in it }.forEach { line ->
        for (stackIndex in 1..9) {
            val lineIndex = 1 + (stackIndex - 1) * 4
            if (line.lastIndex < lineIndex || line[lineIndex] == ' ') continue
            crates[stackIndex] = listOf(line[lineIndex]) + crates.getValue(stackIndex)
        }
    }
    rearrangementInput.filter { "move" in it }.forEach { line ->
        val (amount, from, to) = "\\d+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        if (partTwo) {
            crates[to] = crates.getValue(to) + crates.getValue(from).takeLast(amount)
            crates[from] = crates.getValue(from).dropLast(amount)
        } else {
            repeat(amount) {
                crates[to] = crates.getValue(to) + crates.getValue(from).last()
                crates[from] = crates.getValue(from).dropLast(1)
            }
        }
    }
    return crates.values.map { it.last() }.joinToString("")
}
