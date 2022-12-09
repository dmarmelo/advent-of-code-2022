import kotlin.math.abs

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

    fun Position.movePath(motion: Motion) = buildList {
        repeat(motion.steps) {
            add(
                when (motion.direction) {
                    Direction.UP -> copy(y = y + (it + 1))
                    Direction.DOWN -> copy(y = y - (it + 1))
                    Direction.LEFT -> copy(x = x - (it + 1))
                    Direction.RIGHT -> copy(x = x + (it + 1))
                }
            )
        }
    }

    fun Position.follow(other: Position): Position {
        val (deltaX, deltaY) = other - this
        var newTail: Position = this
        if (abs(deltaX) == 2 && deltaY == 0) {
            newTail = this.copy(x = this.x + (deltaX / abs(deltaX)))
        } else if (abs(deltaY) == 2 && deltaX == 0) {
            newTail = this.copy(y = this.y + (deltaY / abs(deltaY)))
        } else if (abs(deltaX) == 2 || abs(deltaY) == 2) {
            newTail = this.copy(
                x = this.x + (deltaX / abs(deltaX)),
                y = this.y + (deltaY / abs(deltaY))
            )
        }
        return newTail
    }

    fun Motion.moveRope(rope: List<Position>): List<List<Position>> {
        val headPath = rope.first().movePath(this)
        var tail = rope.drop(1)
        return headPath.map { head ->
            tail.fold(listOf(head)) { acc, position ->
                acc + position.follow(acc.last())
            }.also {
                tail = it.drop(1)
            }
        }
    }

    fun List<Motion>.getTailPath(rope: List<Position>): Set<Position> {
        var lastRope = rope
        return flatMap { motion ->
            val moveRope = motion.moveRope(lastRope)
            lastRope = moveRope.last()
            moveRope
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
