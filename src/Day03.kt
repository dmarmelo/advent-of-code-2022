fun main() {
    fun calculatePriority(it: Char) = when (it.code) {
        in ('a'.code..'z'.code) -> it - 'a' + 1
        in ('A'.code..'Z'.code) -> it - 'A' + 27
        else -> 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { rucksack ->
            val secondCompIndex = rucksack.length / 2
            val firstComp = rucksack.substring(0, secondCompIndex).toSet()
            val secondComp = rucksack.substring(secondCompIndex).toSet()
            firstComp.intersect(secondComp)
                .sumOf(::calculatePriority)
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { (rucksack1, rucksack2, rucksack3) ->
            rucksack1.toSet()
                .intersect(rucksack2.toSet())
                .intersect(rucksack3.toSet())
                .sumOf(::calculatePriority)
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
