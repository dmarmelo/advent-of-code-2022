fun main() {
    fun List<String>.elfInventories(): List<List<Int>> {
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
    val testInput = readInput("Day01_test")
    val testInputElfInventories = testInput.elfInventories()
    check(part1(testInputElfInventories) == 24000)
    check(part2(testInputElfInventories) == 45000)

    val input = readInput("Day01")
    val inputElfInventories = input.elfInventories()
    println(part1(inputElfInventories))
    println(part2(inputElfInventories))
}
