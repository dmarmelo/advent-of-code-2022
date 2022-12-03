fun main() {
    fun List<String>.parseInput(): List<List<Int>> {
        var rest = this
        return buildList {
            while (rest.isNotEmpty()) {
                val elfInventory = rest.takeWhile { it.isNotBlank() }.map { it.toInt() }
                add(elfInventory)
                rest =
                    if (elfInventory.size == rest.size) emptyList()
                    else rest.subList(elfInventory.size + 1, rest.size)
            }
        }
    }

    fun part1(elfInventories: List<List<Int>>): Int {
        return elfInventories.maxOf { it.sum() }
    }

    fun part2(elfInventories: List<List<Int>>): Int {
        return elfInventories
            .map { it.sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").parseInput()
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01").parseInput()
    println(part1(input))
    println(part2(input))
}
