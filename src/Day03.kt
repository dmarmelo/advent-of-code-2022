fun main() {
    fun calculatePriority(it: Char) = when (it.code) {
        in ('a'.code..'z'.code) -> it - 'a' + 1
        in ('A'.code..'Z'.code) -> it - 'A' + 27
        else -> 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { rucksack ->
            val secondCompartmentIndex = rucksack.length / 2
            val first = rucksack.substring(0, secondCompartmentIndex).toSet()
            val second = rucksack.substring(secondCompartmentIndex).toSet()
            first.intersect(second)
                .sumOf { calculatePriority(it) }
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { (r1, r2, r3) ->
            r1.toSet()
                .intersect(r2.toSet())
                .intersect(r3.toSet())
                .sumOf { calculatePriority(it) }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
