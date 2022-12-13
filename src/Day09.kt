import kotlin.math.absoluteValue

private typealias Rope = List<Point2D>

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun main() {
    fun String.toDirection() = when (this) {
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        else -> error("Unknown direction $this")
    }

    fun List<String>.parseInput() = flatMap {
        it.split(" ").let { (direction, steps) ->
            List(steps.toInt()) { direction.toDirection() }
        }
    }

    fun Point2D.move(direction: Direction) = when (direction) {
        Direction.UP -> copy(y = y + 1)
        Direction.DOWN -> copy(y = y - 1)
        Direction.LEFT -> copy(x = x - 1)
        Direction.RIGHT -> copy(x = x + 1)
    }

    infix fun Point2D.follow(other: Point2D): Point2D {
        val (diffX, diffY) = other - this
        return if (diffX.absoluteValue <= 1 && diffY.absoluteValue <= 1) this
        else copy(
            x = x + diffX.coerceIn(-1..1),
            y = y + diffY.coerceIn(-1..1)
        )
    }

    infix fun Rope.move(direction: Direction): Rope {
        val head = first().move(direction)
        val tail = drop(1)
        return tail.fold(listOf(head)) { rope, position ->
            rope + (position follow rope.last())
        }
    }

    fun List<Direction>.moveRope(rope: Rope) =
        runningFold(rope) { previousPosition, direction ->
            previousPosition move direction
        }

    fun List<Direction>.getTailPositions(rope: Rope) =
        moveRope(rope).map { it.last() }.toSet()

    fun part1(input: List<Direction>): Int {
        return input.getTailPositions(List(2) { Point2D(0, 0) }).size
    }

    fun part2(input: List<Direction>): Int {
        return input.getTailPositions(List(10) { Point2D(0, 0) }).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test").parseInput()
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09").parseInput()
    println(part1(input))
    println(part2(input))
}
