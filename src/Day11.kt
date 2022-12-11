private typealias Operation = (old: Long) -> Long

private data class MonkeyTest(
    val value: Long,
    val ifTrue: Int,
    val ifFalse: Int
) {
    operator fun invoke(value: Long) =
        if (value % this.value == 0L) ifTrue else ifFalse
}

private data class Monkey(
    val id: Int,
    val items: List<Long>,
    val operation: Operation,
    val test: MonkeyTest
)

private fun createOperation(expression: String): Operation {
    val (operand1, operator, operand2) = """new = (.+) (.+) (.+)""".toRegex().matchEntire(expression)!!.destructured
    return when (operator) {
        "+" -> { old -> (operand1.toLongOrNull() ?: old) + (operand2.toLongOrNull() ?: old) }
        "-" -> { old -> (operand1.toLongOrNull() ?: old) - (operand2.toLongOrNull() ?: old) }
        "*" -> { old -> (operand1.toLongOrNull() ?: old) * (operand2.toLongOrNull() ?: old) }
        "/" -> { old -> (operand1.toLongOrNull() ?: old) / (operand2.toLongOrNull() ?: old) }
        else -> error("Unknown operator $operator")
    }
}

private fun List<String>.toMonkey(): Monkey {
    val number = """Monkey (\d+):""".toRegex().matchEntire(this[0])!!.groupValues[1].toInt()
    val items = this[1].trim().substringAfter("Starting items: ").split(", ").map { it.toLong() }
    val operation = this[2].trim().substringAfter("Operation: ").let(::createOperation)
    val test = this[3].trim().substringAfter("Test: divisible by ").toLong()
    val ifTestTrue = this[4].trim().substringAfter("If true: throw to monkey ").toInt()
    val ifTestFalse = this[5].trim().substringAfter("If false: throw to monkey ").toInt()
    return Monkey(
        number,
        items,
        operation,
        MonkeyTest(test, ifTestTrue, ifTestFalse)
    )
}

fun main() {
    fun <R> List<String>.parts(delimiter: String = "", part: (List<String>) -> R): List<R> = buildList {
        var current = mutableListOf<String>()
        this@parts.forEach {
            if (it == delimiter) {
                add(part(current))
                current = mutableListOf()
            } else {
                current += it
            }
        }
        if (current.isNotEmpty()) {
            add(part(current))
        }
    }

    fun List<String>.parseInput() = parts { it.toMonkey() }

    fun solve(input: List<Monkey>, rounds: Int, processWorry: (Long) -> Long): Long {
        val monkeys = input.toMutableList()
        val monkeyOperations = LongArray(input.size) { 0 }

        repeat(rounds) {
            repeat(monkeys.size) { monkeyNumber ->
                val monkey = monkeys[monkeyNumber]
                monkey.items.forEach { item ->
                    val newItem = processWorry(monkey.operation(item))
                    val throwToMonkey = monkey.test(newItem)
                    val catcherMonkey = monkeys[throwToMonkey]
                    monkeys[throwToMonkey] = catcherMonkey.copy(items = catcherMonkey.items + newItem)
                    monkeyOperations[monkey.id] = monkeyOperations[monkey.id] + 1
                }
                monkeys[monkey.id] = monkey.copy(items = emptyList())
            }
        }
        return monkeyOperations.sortedDescending().take(2).product()
    }

    fun part1(input: List<Monkey>) = solve(input, 20) { it / 3 }

    fun part2(input: List<Monkey>): Long {
        val commonModulus = input.fold(1L) { acc, monkey -> acc * monkey.test.value }
        return solve(input, 10_000) { it % commonModulus }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test").parseInput()
    check(part1(testInput) == 10_605L)
    check(part2(testInput) == 2_713_310_158L)

    val input = readInput("Day11").parseInput()
    println(part1(input))
    println(part2(input))
}
