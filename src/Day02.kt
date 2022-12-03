enum class Gesture(val points: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

fun Gesture.beats() = when (this) {
    Gesture.ROCK -> Gesture.SCISSORS
    Gesture.PAPER -> Gesture.ROCK
    Gesture.SCISSORS -> Gesture.PAPER
}

fun Gesture.beatenBy() = Gesture.values().find { it beats this }!!

infix fun Gesture.beats(other: Gesture) = this.beats() == other

infix fun Gesture.beatenBy(other: Gesture) = this.beatenBy() == other

enum class Outcome(val points: Int) {
    WIN(6),
    DRAW(3),
    LOSS(0)
}

fun main() {
    fun List<String>.parseInput() = map {
        val (a, b) = it.split(' ')
        a.first() to b.first()
    }

    fun Char.toGesture(): Gesture {
        require(this in 'A'..'C' || this in 'X'..'Z')
        return when (this) {
            'A', 'X' -> Gesture.ROCK
            'B', 'Y' -> Gesture.PAPER
            'C', 'Z' -> Gesture.SCISSORS
            else -> error("Unknown input $this")
        }
    }

    fun Char.toOutcome(): Outcome {
        require(this in 'X'..'Z')
        return when (this) {
            'X' -> Outcome.LOSS
            'Y' -> Outcome.DRAW
            'Z' -> Outcome.WIN
            else -> error("Unknown input $this")
        }
    }

    fun calculateOutcome(first: Gesture, second: Gesture): Outcome {
        return if (first == second) Outcome.DRAW
        else if (first beats second) Outcome.WIN
        else Outcome.LOSS
    }

    fun calculateScore(oponentGesture: Gesture, myGesture: Gesture) =
        myGesture.points + calculateOutcome(myGesture, oponentGesture).points

    fun part1(input: List<Pair<Char, Char>>): Int {
        return input.sumOf { (oponenteCode, myCode) ->
            calculateScore(oponenteCode.toGesture(), myCode.toGesture())
        }
    }

    fun part2(input: List<Pair<Char, Char>>): Int {
        return input.sumOf { (oponenteCode, gameResultCode) ->
            val oponentGesture = oponenteCode.toGesture()
            val myGesture = when (gameResultCode.toOutcome()) {
                Outcome.DRAW -> oponentGesture
                Outcome.LOSS -> oponentGesture.beats()
                Outcome.WIN -> oponentGesture.beatenBy()
            }
            calculateScore(oponentGesture, myGesture)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test").parseInput()
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02").parseInput()
    println(part1(input))
    println(part2(input))
}
