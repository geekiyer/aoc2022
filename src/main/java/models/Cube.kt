package models

data class Cube(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {

    data class FaceTransition(val newX: Int, val newY: Int, val newCubeDirection: CubeDirection, var newFace: Cube)

    companion object {
        val FACE_ONE = Cube(50, 99, 0, 49)
        val FACE_TWO = Cube(100, 149, 0, 49)
        val FACE_THREE = Cube(50, 99, 50, 99)
        val FACE_FOUR = Cube(0, 49, 100, 149)
        val FACE_FIVE = Cube(50, 99, 100, 149)
        val FACE_SIX = Cube(0, 49, 150, 199)

        fun leftToRight(y: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX
            val newY = destination.maxY - (y - source.minY)
            val newCubeDirection = CubeDirection.RIGHT

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun leftToDown(y: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX + (y - source.minY)
            val newY = destination.minY
            val newCubeDirection = CubeDirection.DOWN

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun rightToLeft(y: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.maxX
            val newY = destination.maxY - (y - source.minY)
            val newCubeDirection = CubeDirection.LEFT

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun rightToUp(y: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX + (y - source.minY)
            val newY = destination.maxY
            val newCubeDirection = CubeDirection.UP

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun upToRight(x: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX
            val newY = destination.minY + (x - source.minX)
            val newCubeDirection = CubeDirection.RIGHT

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun upToUp(x: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX + (x - source.minX)
            val newY = destination.maxY
            val newCubeDirection = CubeDirection.UP

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun downToLeft(x: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.maxX
            val newY = destination.minY + (x - source.minX)
            val newCubeDirection = CubeDirection.LEFT

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }

        fun downToDown(x: Int, source: Cube, destination: Cube): FaceTransition {
            val newX = destination.minX + (x - source.minX)
            val newY = destination.minY
            val newCubeDirection = CubeDirection.DOWN

            return FaceTransition(newX, newY, newCubeDirection, destination)
        }
    }
}

enum class CubeDirection(private val dX: Int, private val dY: Int, val score: Int) {
    RIGHT(1, 0,0) {
        override fun turnLeft() = UP
        override fun turnRight() = DOWN
    },
    DOWN(0, 1, 1) {
        override fun turnLeft() = RIGHT
        override fun turnRight() = LEFT
    },
    LEFT(-1, 0, 2) {
        override fun turnLeft() = DOWN
        override fun turnRight() = UP
    },
    UP(0, -1, 3) {
        override fun turnLeft() = LEFT
        override fun turnRight() = RIGHT
    };

    abstract fun turnLeft(): CubeDirection
    abstract fun turnRight(): CubeDirection

    fun dX(x: Int) = x + dX
    fun dY(y: Int) = y + dY
}