fun main() {
    fun List<String>.parseInput() = map {
        it.map { c -> c.digitToInt() }
    }

    fun <T> List<List<T>>.viewFrom(rowIndex: Int, columnIndex: Int): Sequence<List<T>> = sequence {
        yield((rowIndex - 1 downTo 0).map { this@viewFrom[it][columnIndex] }) // Top
        yield((columnIndex - 1 downTo 0).map { this@viewFrom[rowIndex][it] }) // Right
        yield((rowIndex + 1 until this@viewFrom.size).map { this@viewFrom[it][columnIndex] }) // Bottom
        yield((columnIndex + 1 until this@viewFrom[0].size).map { this@viewFrom[rowIndex][it] }) // Left
    }

    fun part1(input: List<List<Int>>): Int {
        var visibleTreeCount = 0
        visibleTreeCount += input.size * 2 + input[0].size * 2 - 4

        for (rowIndex in 1 until input.lastIndex) {
            val row = input[rowIndex]
            for (columnIndex in 1 until row.lastIndex) {
                val currentHeight = row[columnIndex]

                val isVisible = input.viewFrom(rowIndex, columnIndex).any { direction ->
                    direction.all { it < currentHeight }
                }
                if (isVisible) {
                    visibleTreeCount++
                }
            }
        }

        return visibleTreeCount
    }

    fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
        val list = ArrayList<T>()
        for (item in this) {
            list.add(item)
            if (!predicate(item))
                break
        }
        return list
    }

    fun part2(input: List<List<Int>>): Int {
        var scenicScore = 0

        for ((rowIndex, row) in input.withIndex()) {
            for ((columnIndex, _) in row.withIndex()) {
                val currentHeight = row[columnIndex]

                val score = input.viewFrom(rowIndex, columnIndex).map { direction ->
                    direction.takeUntil { it < currentHeight }.count()
                }.product()
                if (score > scenicScore) {
                    scenicScore = score
                }
            }
        }

        return scenicScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test").parseInput()
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08").parseInput()
    println(part1(input))
    println(part2(input))
}
