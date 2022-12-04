fun main() {
    fun String.toIntRange(): IntRange {
        val (start, end) = split("-")
        return start.toInt()..end.toInt()
    }

    fun List<String>.parseInput() = map {
        val (range1, range2) = it.split(',')
        range1.toIntRange() to range2.toIntRange()
    }

    operator fun IntRange.contains(other: IntRange) =
        first <= other.first && last >= other.last

    infix fun IntRange.overlaps(other: IntRange) =
        first <= other.last && last >= other.first

    fun part1(input: List<Pair<IntRange, IntRange>>): Int {
        return input.count { (range1, range2) ->
            range1 in range2 || range2 in range1
        }
    }

    fun part2(input: List<Pair<IntRange, IntRange>>): Int {
        return input.count { (range1, range2) ->
            range1 overlaps range2
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test").parseInput()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04").parseInput()
    println(part1(input))
    println(part2(input))
}
