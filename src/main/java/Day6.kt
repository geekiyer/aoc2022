fun main() {
    println(sol(true))
    println(sol(false))
}

val packet = object {}.javaClass.getResourceAsStream("day6.txt")?.bufferedReader()?.readLines()!!

fun sol(isPart1: Boolean): Int {
    val line = packet.first()
    val n = if (isPart1) 4 else 14
    return n + (0 until line.length - n).first { line.substring(it until it + n).toCharArray().distinct().size == n }
}