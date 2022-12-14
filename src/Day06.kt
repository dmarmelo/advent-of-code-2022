fun main() {
    fun CharSequence.allUnique() = toHashSet().size == length

    fun findMarker(width: Int, input: String) =
        input.windowedSequence(width)
            .indexOfFirst { it.allUnique() } + width

    fun part1(input: String): Int {
        return findMarker(4, input)
    }

    fun part2(input: String): Int {
        return findMarker(14, input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputText("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInputText("Day06")
    println(part1(input))
    println(part2(input))
}
