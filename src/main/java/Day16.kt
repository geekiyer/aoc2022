private const val PART_A_MINUTES = 30
private const val PART_B_MINUTES = 26

fun main() {
    val input = object {}.javaClass.getResourceAsStream("day16.txt")?.bufferedReader().use { it?.readText() }?.trimIndent()
    println(Day16().partOne(input!!))
    println(Day16().partTwo(input!!))
}

class Day16 {
    fun partOne(input: String) = parseInput(input).let { allValves ->
        val maxOpenedValves = allValves.values.count { it.flowRate > 0 }

        val start = allValves["AA"] ?: error("valve AA not found")
        val startPath = PathA(
            valves = listOf(start),
            opened = HashMap(),
        )
        var allPaths = listOf(startPath)
        var bestPath = startPath

        var time = 1

        while (time < PART_A_MINUTES) {
            val newPaths = arrayListOf<PathA>()

            for (currentPath in allPaths) {
                if (currentPath.opened.size == maxOpenedValves) {
                    continue
                }

                val currentLast = currentPath.last()
                val currentValves = currentPath.valves

                // open valve
                if (currentLast.flowRate > 0 && !currentPath.opened.containsKey(currentLast)) {
                    val opened = currentPath.opened.toMutableMap()
                    opened[currentLast] = time
                    val possibleValves = currentValves + currentLast
                    val possibleOpenedPath = PathA(possibleValves, opened)
                    newPaths.add(possibleOpenedPath)
                }

                // move to valve
                val possiblePaths: List<PathA> = currentLast.leadsTo.map { lead ->
                    val possibleValve = allValves[lead] ?: error("valve $lead not found")
                    val possibleValves = currentValves + possibleValve
                    val possiblePath = PathA(possibleValves, currentPath.opened)
                    possiblePath
                }

                newPaths.addAll(possiblePaths)
            }

            allPaths = newPaths.sortedByDescending { it.total() }.take(10000)

            if (allPaths.first().total() > bestPath.total()) {
                bestPath = allPaths.first()
            }

            time++
        }

        bestPath.total()
    }

    fun partTwo(input: String) = parseInput(input).let { allValves ->
        val maxOpenedValves = allValves.values.count { it.flowRate > 0 }

        val start = allValves["AA"] ?: error("valve AA not found")
        val startPath = PathB(
            valvesMe = listOf(start),
            valvesElephant = listOf(start),
            opened = HashMap(),
        )
        var allPaths = listOf(startPath)
        var bestPath = startPath

        var time = 1

        while (time < PART_B_MINUTES) {
            val newPaths = arrayListOf<PathB>()

            for (currentPath in allPaths) {
                if (currentPath.opened.size == maxOpenedValves) {
                    continue
                }

                val currentLastMe = currentPath.lastMe()
                val currentLastElephant = currentPath.lastElephant()
                val currentValvesMe = currentPath.valvesMe
                val currentValvesElephant = currentPath.valvesElephant

                val openMe = currentLastMe.flowRate > 0 && !currentPath.opened.containsKey(currentLastMe)
                val openElephant =
                    currentLastElephant.flowRate > 0 && !currentPath.opened.containsKey(currentLastElephant)

                if (openMe || openElephant) {
                    val opened = currentPath.opened.toMutableMap()

                    val possibleValvesMes: List<List<Valve>> = if (openMe) {
                        opened[currentLastMe] = time
                        listOf(currentValvesMe + currentLastMe)
                    } else {
                        currentLastMe.leadsTo.map { lead ->
                            val possibleValve = allValves[lead] ?: error("valve $lead not found")
                            val possibleValves = currentValvesMe + possibleValve
                            possibleValves
                        }
                    }

                    val possibleValvesElephants: List<List<Valve>> = if (openElephant) {
                        opened[currentLastElephant] = time
                        listOf(currentValvesElephant + currentLastElephant)
                    } else {
                        currentLastElephant.leadsTo.map { lead ->
                            val possibleValve = allValves[lead] ?: error("valve $lead not found")
                            val possibleValves = currentValvesElephant + possibleValve
                            possibleValves
                        }
                    }

                    for (possibleValvesMe in possibleValvesMes) {
                        for (possibleValvesElephant in possibleValvesElephants) {
                            val possibleOpenedPath = PathB(possibleValvesMe, possibleValvesElephant, opened)
                            newPaths.add(possibleOpenedPath)
                        }
                    }
                }

                val combinedLeads = currentLastMe.leadsTo.map { leadMe ->
                    currentLastElephant.leadsTo.map { leadElephant ->
                        leadMe to leadElephant
                    }
                }
                    .flatten()
                    .filter { (a, b) -> a != b }

                val possiblePaths: List<PathB> = combinedLeads.map { (leadMe, leadElephant) ->
                    val possibleValveMe = allValves[leadMe] ?: error("valve $leadMe not found")
                    val possibleValvesMe = currentValvesMe + possibleValveMe
                    val possibleValveElephant = allValves[leadElephant] ?: error("valve $leadElephant not found")
                    val possibleValvesElephant = currentValvesElephant + possibleValveElephant
                    val possiblePath = PathB(possibleValvesMe, possibleValvesElephant, currentPath.opened)
                    possiblePath
                }

                newPaths.addAll(possiblePaths)
            }

            allPaths = newPaths.sortedByDescending { it.total() }.take(100000)

            if (allPaths.first().total() > bestPath.total()) {
                bestPath = allPaths.first()
            }
            time++
        }
        bestPath.total()
    }

    private fun parseInput(input: String) = input
        .split("\n")
        .asSequence()
        .map {
            it.replace("Valve ", "")
                .replace("has flow rate=", "")
                .replace("has flow rate=", "")
                .replace("; tunnels lead to valves", "")
                .replace("; tunnel leads to valve", "")
                .split(' ', ',')
                .filter { it.isNotEmpty() }
        }
        .let {
            it.associate {
                val name = it[0]
                val flowRate = it[1].toInt()
                val leadsTo = it.subList(2, it.size)
                name to Valve(flowRate, leadsTo)
            }
        }
}

data class Valve(val flowRate: Int, val leadsTo: List<String>)

data class PathA(val valves: List<Valve>, val opened: Map<Valve, Int>) {

    fun last(): Valve = valves.last()

    fun total(): Int = opened.map { (valve, time) -> (PART_A_MINUTES - time) * valve.flowRate }.sum()

}

data class PathB(val valvesMe: List<Valve>, val valvesElephant: List<Valve>, val opened: Map<Valve, Int>) {

    fun lastMe(): Valve = valvesMe.last()

    fun lastElephant(): Valve = valvesElephant.last()

    fun total(): Int = opened.map { (valve, time) -> (PART_B_MINUTES - time) * valve.flowRate }.sum()

}