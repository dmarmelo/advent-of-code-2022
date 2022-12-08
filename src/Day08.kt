fun main() {
    fun List<String>.parseInput() = map {
        it.map { c -> c.digitToInt() }
    }

    fun part1(input: List<List<Int>>): Int {
        var visibleTreeCount = 0

        visibleTreeCount += input.size * 2 + (input[0].size - 2) * 2

        for (row in 1 until input.lastIndex) {
            val rowList = input[row]
            for (column in 1 until rowList.lastIndex) {
                val columnList = buildList {
                    repeat(rowList.size) {
                        add(input[it][column])
                    }
                }
                val height = rowList[column]

                val left = rowList.subList(0, column)
                if (left.all { it < height }) {
                    visibleTreeCount++
                    continue
                }
                val right = rowList.subList(column + 1, rowList.size)
                if (right.all { it < height }) {
                    visibleTreeCount++
                    continue
                }

                val top = columnList.subList(0, row)
                if (top.all { it < height }) {
                    visibleTreeCount++
                    continue
                }
                val bottom = columnList.subList(row + 1, columnList.size)
                if (bottom.all { it < height }) {
                    visibleTreeCount++
                }
            }
        }

        return visibleTreeCount
    }

    fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
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

        for (row in 0..input.lastIndex) {
            val rowList = input[row]
            for (column in 0..rowList.lastIndex) {
                val columnList = buildList {
                    repeat(rowList.size) {
                        add(input[it][column])
                    }
                }
                val height = rowList[column]

                val left = rowList.subList(0, column).reversed().takeWhileInclusive { it < height }.size
                val right = rowList.subList(column + 1, rowList.size).takeWhileInclusive { it < height }.size
                val top = columnList.subList(0, row).reversed().takeWhileInclusive { it < height }.size
                val bottom = columnList.subList(row + 1, columnList.size).takeWhileInclusive { it < height }.size

                val score = left * right * top * bottom
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
