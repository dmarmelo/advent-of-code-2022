import kotlin.math.absoluteValue
import kotlin.math.sign

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

private data class Motion(
    val direction: Direction,
    val steps: Int
)

private data class Position(
    val x: Int,
    val y: Int
) {
    operator fun minus(other: Position) =
        Position(x - other.x, y - other.y)
}

fun main() {
    fun String.toDirection() = when (this) {
        "U" -> Direction.UP
        "D" -> Direction.DOWN
        "L" -> Direction.LEFT
        "R" -> Direction.RIGHT
        else -> error("Unknown direction $this")
    }

    fun List<String>.parseInput() = map {
        val (direction, steps) = it.split(" ")
        Motion(direction.toDirection(), steps.toInt())
    }

    fun Position.move(direction: Direction) =
        when (direction) {
            Direction.UP -> copy(y = y + 1)
            Direction.DOWN -> copy(y = y - 1)
            Direction.LEFT -> copy(x = x - 1)
            Direction.RIGHT -> copy(x = x + 1)
        }


    fun Position.follow(other: Position): Position {
        val (deltaX, deltaY) = other - this
        return if (deltaX.absoluteValue <= 1 && deltaY.absoluteValue <= 1) this
        else this.copy(
            x = deltaX.sign + x,
            y = deltaY.sign + y
        )
    }

    fun Motion.moveRope(rope: List<Position>): List<List<Position>> = buildList {
        var workingRope = rope
        repeat(steps) {
            val head = workingRope.first().move(direction)
            val tail = workingRope.drop(1)
            workingRope = tail.fold(listOf(head)) { acc, position ->
                acc + position.follow(acc.last())
            }
            add(workingRope)
        }
    }

    fun List<Motion>.getTailPath(rope: List<Position>): Set<Position> {
        var lastRope = rope
        return flatMap { motion ->
            val ropePath = motion.moveRope(lastRope)
            lastRope = ropePath.last()
            ropePath
        }.map { it.last() }.toSet()
    }

    fun part1(input: List<Motion>): Int {
        return input.getTailPath(List(2) { Position(0, 0) }).size
    }

    fun part2(input: List<Motion>): Int {
        return input.getTailPath(List(10) { Position(0, 0) }).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test").parseInput()
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09").parseInput()
    println(part1(input))
    println(part2(input))
}
