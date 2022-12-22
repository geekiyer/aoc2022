fun main() {
    val input = object {}.javaClass.getResourceAsStream("day20.txt")?.bufferedReader()?.readLines()!!
    println(Day20(input).part1())
    println(Day20(input).part2())
}

class Day20(private val input: List<String>) {

    private data class MappedNumber(val originalIndex: Int, val value: Long)

    fun part1(): Long {
        val theList = parseInput()
        theList.decrypt()
        return theList.groveCoordinates()
    }

    fun part2(): Long {
        val theList = parseInput(811_589_153)
        repeat(10) {
            theList.decrypt()
        }
        return theList.groveCoordinates()
    }

    private fun parseInput(decryptionKey: Long = 1L): MutableList<MappedNumber> =
        input.mapIndexed { index, value -> MappedNumber(index, decryptionKey * value.toLong()) }.toMutableList()

    private fun List<MappedNumber>.groveCoordinates(): Long {
        val zero = indexOfFirst { it.value == 0L }
        return listOf(1000, 2000, 3000).sumOf { this[(zero + it) % size].value }
    }

    private fun MutableList<MappedNumber>.decrypt() {
        indices.forEach { originalIndex ->
            val index = indexOfFirst { it.originalIndex == originalIndex }
            val toBeMoved = removeAt(index)
            add((index + toBeMoved.value).mod(size), toBeMoved)
        }
    }
}