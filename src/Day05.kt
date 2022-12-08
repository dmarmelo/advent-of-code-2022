typealias Stack = Pair<Int, List<Char>>

private data class Move(
    val quantity: Int,
    val from: Int,
    val to: Int
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

    fun String.parseStacks(): List<Stack> = lines()
        .map { line -> line.windowed(size = 3, step = 4) { it[1] } }
        .transpose()
        .map { it.filter { char -> !char.isWhitespace() } }
        .map { it[0].digitToInt() to it.drop(1) }

    fun String.parseMoves(): List<Move> =
        Regex("""^move (\d+) from (\d+) to (\d+)$""", RegexOption.MULTILINE)
            .findAll(this).map {
                val (quantity, fromPosition, toPosition) = it.destructured
                Move(quantity.toInt(), fromPosition.toInt(), toPosition.toInt())
            }.toList()

    fun String.parseInput(): Pair<List<Stack>, List<Move>> {
        val (stacks, procedure) = split("\n\n", "\r\n\r\n")
        return stacks.parseStacks() to procedure.parseMoves()
    }

    fun List<Stack>.toMutable() = map { (position, items) ->
        position to items.toMutableList()
    }

    fun List<Stack>.joinTopItems() = joinToString("") { (_, items) ->
        items.last().toString()
    }

    fun part1(input: Pair<List<Stack>, List<Move>>): String {
        val (stacks, moves) = input

        val workingStacks = stacks.toMutable()

        moves.forEach {
            val (_, fromStack) = workingStacks[it.from - 1]
            val (_, toStack) = workingStacks[it.to - 1]
            repeat(it.quantity) {
                toStack.add(fromStack.removeLast())
            }
        }

        return workingStacks.joinTopItems()
    }

    fun part2(input: Pair<List<Stack>, List<Move>>): String {
        val (stacks, moves) = input

        val workingStacks = stacks.toMutable()

        moves.forEach {
            val (_, fromStack) = workingStacks[it.from - 1]
            val (_, toStack) = workingStacks[it.to - 1]
            val cratesToMove = fromStack.takeLast(it.quantity)
            repeat(it.quantity) { fromStack.removeLast() }
            toStack.addAll(cratesToMove)
        }

        return workingStacks.joinTopItems()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputText("Day05_test").parseInput()
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInputText("Day05").parseInput()
    println(part1(input))
    println(part2(input))
}
