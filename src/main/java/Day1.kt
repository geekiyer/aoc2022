fun main() {
    // partOne()
    partTwo()
}

fun partOne() {
    try {
        val inputLines = object {}.javaClass.getResourceAsStream("day1.txt")?.bufferedReader()?.readLines()
        var max = 0
        var curr = 0
        if (inputLines != null) {
            for (line in inputLines) {
                if (line == "") {
                    max = max.coerceAtLeast(curr)
                    curr = 0
                    continue
                }
                curr += Integer.parseInt(line)
            }
        }
        println(max)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun partTwo() {
    try {
        val inputLines = object {}.javaClass.getResourceAsStream("day1.txt")?.bufferedReader()?.readLines()
        val list = mutableListOf<Int>()
        var curr = 0
        if (inputLines != null) {
            for (line in inputLines) {
                if (line == "") {
                    list.add(curr)
                    curr = 0
                    continue
                }
                curr += Integer.parseInt(line)
            }
        }
        list.add(curr)
        list.sortDescending()
        println(list[0] + list[1] + list[2])
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
