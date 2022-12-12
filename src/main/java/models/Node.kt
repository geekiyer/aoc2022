package models

sealed class Node(open val x: Int, open val y: Int, open val elev: Char) {
    data class Start(
        override val x: Int,
        override val y: Int,
        override val elev: Char = 'a'
    ) :
        Node(x, y, elev)

    data class End(
        override val x: Int,
        override val y: Int,
        override val elev: Char = 'z'
    ) :
        Node(x, y, elev)

    data class Other(
        override val x: Int,
        override val y: Int,
        override val elev: Char
    ) :
        Node(x, y, elev)
}

data class Grid(val start: Node, val end: Node, val nodes: List<List<Node>>)