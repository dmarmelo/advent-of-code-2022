fun main() {
    fun List<String>.parseInput() = map {
        val (range1, range2) = it.split(',')
        val (range1Start, range1End) = range1.split("-")
        val (range2Start, range2End) = range2.split("-")
        (range1Start.toInt()..range1End.toInt()) to (range2Start.toInt()..range2End.toInt())
    }

    operator fun IntRange.contains(other: IntRange) =
        first <= other.first && last >= other.last

    fun part1(input: List<Pair<IntRange, IntRange>>): Int {
        return input.fold(0) { acc, (range1, range2) ->
            if (range1 in range2 || range2 in range1) acc + 1
            else acc
        }
    }

    fun part2(input: List<Pair<IntRange, IntRange>>): Int {
        return input.fold(0) { acc, (range1, range2) ->
            if (range1.intersect(range2).isNotEmpty()) acc + 1
            else acc
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
