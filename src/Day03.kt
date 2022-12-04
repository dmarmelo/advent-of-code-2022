fun main() {
    fun calculatePriority(char: Char) = when (char.code) {
        in ('a'.code..'z'.code) -> char - 'a' + 1
        in ('A'.code..'Z'.code) -> char - 'A' + 27
        else -> error("Unknown input $char")
    }

    infix fun String.intersect(other: String): Set<Char> = toSet() intersect other.toSet()
    infix fun Iterable<Char>.intersect(other: String) = this intersect other.toSet()

    fun part1(input: List<String>): Int {
        return input.sumOf { rucksack ->
            val firstComp = rucksack.substring(0, rucksack.length / 2)
            val secondComp = rucksack.substring(rucksack.length / 2)
            (firstComp intersect secondComp)
                .sumOf(::calculatePriority)
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { (rucksack1, rucksack2, rucksack3) ->
                (rucksack1 intersect rucksack2 intersect rucksack3)
                    .sumOf(::calculatePriority)
            }
    }

    fun part2_2(input: List<String>): Int {
        return input.chunked(3)
            .flatMap { elfGroup ->
                elfGroup.map { it.toSet() }
                    .reduce { acc, rucksack ->
                        acc intersect rucksack
                    }
            }
            .sumOf(::calculatePriority)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)
    check(part2_2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
    println(part2_2(input))
}
