private typealias Instruction = Pair<String, Int?>

fun main() {
    fun List<String>.parseInput(): List<Instruction> = map {
        if (it.contains(" ")) {
            it.split(" ").let { (instruction, argument) ->
                instruction to argument.toInt()
            }
        } else {
            it to null
        }
    }

    fun List<Instruction>.run(onCycle: (cycle: Int, x: Int) -> Unit) {
        val iterator = iterator()
        var line: Instruction? = null
        var x = 1
        var instructionCycles = 0
        for (clock in (1..240)) {
            if (instructionCycles == 0) {
                line = iterator.next()
                val (instruction, _) = line
                when (instruction) {
                    "noop" -> instructionCycles = 1
                    "addx" -> instructionCycles = 2
                }
            }

            onCycle(clock, x)

            instructionCycles--

            if (instructionCycles == 0 && line != null) {
                val (instruction, argument) = line
                if (instruction == "addx") {
                    x += argument!!
                }
            }
        }
    }

    fun part1(input: List<Instruction>): Int {
        var signalStrength = 0
        input.run { cycle, x ->
            if (cycle in (20..220 step 40)) {
                signalStrength += cycle * x
            }
        }
        return signalStrength
    }

    fun part2(input: List<Instruction>): String {
        val crtWidth = 40
        val crt = ""
        input.run { cycle, x ->
            val crtHorizontalPosition = (cycle - 1) % crtWidth
            print(
                if (crtHorizontalPosition in (x - 1..x + 1)) '#'
                else '.'
            )
            if (crtHorizontalPosition == crtWidth - 1) println()
        }

        return crt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test").parseInput()
    check(part1(testInput) == 13140)
    println(part2(testInput))

    val input = readInput("Day10").parseInput()
    println(part1(input))
    println(part2(input))
}
