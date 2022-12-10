private sealed class Instruction {
    abstract val cycles: Int

    object Noop : Instruction() {
        override val cycles: Int
            get() = 1
    }

    data class Addx(val argument: Int) : Instruction() {
        override val cycles: Int
            get() = 2
    }
}

fun main() {
    fun String.toInstruction() = with(this) {
        when {
            equals("noop") -> Instruction.Noop
            startsWith("addx") -> Instruction.Addx(removePrefix("addx ").toInt())
            else -> error("Unknown instriction $this")
        }
    }

    fun List<String>.parseInput(): List<Instruction> = map { it.toInstruction() }

    fun List<Instruction>.run(onCycle: (cycle: Int, x: Int) -> Unit) {
        val iterator = iterator()

        // Load first Instruction
        var instruction = iterator.next()
        var remainigCycles = instruction.cycles

        var x = 1

        for (cycle in (1..240)) {
            onCycle(cycle, x)
            remainigCycles--
            if (remainigCycles == 0) {
                // Execute Instruction
                if (instruction is Instruction.Addx) {
                    x += instruction.argument
                }

                // Load next Instruction if there is one
                if (iterator.hasNext()) {
                    instruction = iterator.next()
                    remainigCycles = instruction.cycles
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
        var crt = ""
        input.run { cycle, x ->
            val crtHorizontalPosition = (cycle - 1) % crtWidth
            crt += if (crtHorizontalPosition in (x - 1..x + 1)) "#" else "."
            if (crtHorizontalPosition == crtWidth - 1) crt += "\n"
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
