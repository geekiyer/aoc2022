import Util.manhattanDistance
import kotlin.math.abs

fun main() {
    val input = object {}.javaClass.getResourceAsStream("day15.txt")?.bufferedReader()?.readLines()!!
    println(Day15().partOne(row = 2000000, input))
    println(Day15().partTwo(maxCoordinate = 4000000, input))
}

class Day15 {
    class Sensor(val coordinates: Cell, val beacon: Cell) {
        private val beaconDistance: Int = manhattanDistance(coordinates, beacon)
        private fun distanceTo(cell: Cell): Int = manhattanDistance(coordinates, cell)

        fun covers(cell: Cell): Boolean {
            return beaconDistance >= distanceTo(cell)
        }

        fun maxColumnCovered(row: Int): Cell {
            return Cell(row, coordinates.column + abs(beaconDistance - abs(row - coordinates.row)))
        }

        fun minColumnCovered(row: Int): Cell {
            return Cell(row, coordinates.column - abs(beaconDistance - abs(row - coordinates.row)))
        }
    }

    class Sensors(input: List<String>) {
        private val beacons: Set<Cell>
        val sensorMap: Map<Cell, Sensor>

        fun sensorsOnRow(row: Int, minColumn: Int, maxColumn: Int): List<Sensor> {
            return sensorMap.values.filter { it.coordinates.row == row && it.coordinates.column >= minColumn && it.coordinates.column <= maxColumn }
        }

        fun beaconsOnRow(row: Int, minColumn: Int, maxColumn: Int): List<Cell> {
            return beacons.filter { it.row == row && it.column >= minColumn && it.column <= maxColumn }
        }

        init {
            val regex = Regex("-?\\d+")
            sensorMap = input.map {
                regex.findAll(it).map { it.value.toInt() }.toList()
            }
                .map {
                    Sensor(Cell(it[1], it[0]), Cell(it[3], it[2]))
                }.associateBy { it.coordinates }
            beacons = sensorMap.values.map { it.beacon }.toSet()
        }

        fun coveredBySensor(cell: Cell): Sensor? {
            return sensorMap.values.find { it.covers(cell) }
        }
    }

    fun partOne(row: Int, inputList: List<String>): Int {
        val sensors = Sensors(inputList)
        val minColumn = sensors.sensorMap.values.map { it.minColumnCovered(row) }.minOfOrNull { it.column }
        val maxColumn = sensors.sensorMap.values.map { it.maxColumnCovered(row) }.maxOfOrNull { it.column }
        var currentCell = Cell(row, minColumn!!)
        var count: Int = 0
        while (currentCell.column <= maxColumn!!) {
            val coveredBySensor = sensors.coveredBySensor(currentCell)
            if (coveredBySensor != null) {
                val maxColumnCovered = coveredBySensor.maxColumnCovered(currentCell.row)
                count += maxColumnCovered.column - currentCell.column + 1
                count -= sensors.sensorsOnRow(currentCell.row, currentCell.column, maxColumnCovered.column).size
                count -= sensors.beaconsOnRow(currentCell.row, currentCell.column, maxColumnCovered.column).size
                currentCell = maxColumnCovered
            }
            currentCell = currentCell.right()
        }
        return count
    }

    private fun notCoveredCell(maxCoordinate: Int, inputList: List<String>): Cell? {
        val sensors = Sensors(inputList)
        for (row in 0..maxCoordinate) {
            var currentCell = Cell(row, 0)
            while (currentCell.column <= maxCoordinate) {
                val coveredBySensor = sensors.coveredBySensor(currentCell)
                if (coveredBySensor != null) {
                    currentCell = coveredBySensor.maxColumnCovered(currentCell.row).right()
                } else {
                    return currentCell
                }
            }
        }
        return null
    }

    fun partTwo(maxCoordinate: Int, inputList: List<String>): Any {
        val notCoveredCell = notCoveredCell(maxCoordinate, inputList)!!
        return notCoveredCell.column.toLong() * 4000000 + notCoveredCell.row.toLong()
    }
}

object Util {
    fun manhattanDistance(c1: Cell, c2: Cell): Int =
        abs(c1.row - c2.row) + abs(c1.column - c2.column)
}

data class Cell(val row: Int, val column: Int) {
    fun right(): Cell = Cell(row, column + 1)
}