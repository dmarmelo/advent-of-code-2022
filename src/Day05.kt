typealias Stack = Pair<Int, List<Char>>

private data class Move(
    val quantity: Int,
    val fromPosition: Int,
    val toPosition: Int
)

fun main() {
    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return buildList {
            for (column in this@transpose[0].indices) {
                add(
                    buildList {
                        for (row in this@transpose.indices) {
                            add(this@transpose[this@transpose.lastIndex - row][column])
                        }
                    }
                )
            }
        }
    }

    fun String.parseInput(): Pair<List<Stack>, List<Move>> {
        val (currentStacks, rearangementProcedure) = split("\n\n", "\r\n\r\n")

        val stacks = currentStacks.lines()
            .map { line ->
                line.windowed(size = 3, step = 4) { it[1] }
            }
            .transpose()
            .map { it.filter { char -> !char.isWhitespace() } }
            .map { it[0].digitToInt() to it.subList(1, it.size).toMutableList() }

        val moveRegex = """^move (\d+) from (\d+) to (\d+)$""".toRegex(RegexOption.MULTILINE)
        val moves = moveRegex.findAll(rearangementProcedure).map {
            val (quantity, fromPosition, toPosition) = it.destructured
            Move(quantity.toInt(), fromPosition.toInt(), toPosition.toInt())
        }.toList()

        return stacks to moves
    }

    fun part1(input: Pair<List<Stack>, List<Move>>): String {
        val (stacks, moves) = input

        val mutableStacks = stacks.map { (position, items) -> position to items.toMutableList() }

        moves.forEach {
            val (_, fromStack) = mutableStacks[it.fromPosition - 1]
            val (_, toStack) = mutableStacks[it.toPosition - 1]
            repeat(it.quantity) {
                toStack.add(fromStack.removeLast())
            }
        }

        return mutableStacks.map { (_, items) -> items.last() }
            .joinToString("")
    }

    fun part2(input: Pair<List<Stack>, List<Move>>): String {
        val (stacks, moves) = input

        val mutableStacks = stacks.map { (position, items) -> position to items.toMutableList() }

        moves.forEach {
            val (_, fromStack) = mutableStacks[it.fromPosition - 1]
            val (_, toStack) = mutableStacks[it.toPosition - 1]
            val cratesToMove = fromStack.subList(
                fromStack.size - it.quantity,
                fromStack.size
            ).toList()
            repeat(it.quantity) {
                fromStack.removeLast()
            }
            toStack.addAll(cratesToMove)
        }

        return mutableStacks
            .filter { (_, items) -> items.isNotEmpty() }
            .map { (_, items) -> items.last() }
            .joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputText("Day05_test").parseInput()
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInputText("Day05").parseInput()
    println(part1(input))
    println(part2(input))
}
