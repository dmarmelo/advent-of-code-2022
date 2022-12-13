import java.util.*

private data class HeightMap(
    val elevations: Map<Point2D, Int>,
    val start: Point2D,
    val end: Point2D
) {
    fun getNeighbors(point: Point2D) =
        point.cardinalNeighbors.filter { n -> n in elevations }
}

data class CostItem<T>(
    val item: T,
    val cost: Int
) : Comparable<CostItem<T>> {
    override fun compareTo(other: CostItem<T>) =
        cost.compareTo(other.cost)
}

fun main() {
    fun List<String>.parseInput(): HeightMap {
        var start: Point2D? = null
        var end: Point2D? = null
        val elevations = flatMapIndexed { y, row ->
            row.mapIndexed { x, char ->
                val point = Point2D(x, y)
                point to when (char) {
                    'S' -> 0.also { start = point }
                    'E' -> ('z' - 'a').also { end = point }
                    else -> char - 'a'
                }
            }
        }.toMap()
        return HeightMap(elevations, start!!, end!!)
    }

    fun <T> breathFirstSearch(
        start: T,
        isGoal: (T) -> Boolean,
        getNeighbors: (T) -> Iterable<T>,
        canMove: (from: T, to: T) -> Boolean = { _, _ -> true }
    ): Int {
        val seen = mutableSetOf<T>()
        val queue = PriorityQueue<CostItem<T>>().apply { add(CostItem(start, 0)) }

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next.item !in seen) {
                seen += next.item
                val neighbors = getNeighbors(next.item).filter { canMove(next.item, it) }
                if (neighbors.any(isGoal)) {
                    return next.cost + 1
                }
                queue.addAll(neighbors.map { CostItem(it, next.cost + 1) })
            }
        }
        return -1
    }

    fun part1(map: HeightMap): Int {
        return breathFirstSearch(
            start = map.start,
            isGoal = { it == map.end },
            getNeighbors = { map.getNeighbors(it) },
            canMove = { from, to -> map.elevations[to]!! - map.elevations[from]!! <= 1 }
        )
    }

    fun part2(map: HeightMap): Int {
        return breathFirstSearch(
            start = map.end,
            isGoal = { map.elevations[it]!! == 0 },
            getNeighbors = { map.getNeighbors(it) },
            canMove = { from, to -> map.elevations[from]!! - map.elevations[to]!! <= 1 }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test").parseInput()
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12").parseInput()
    println(part1(input))
    println(part2(input))
}
