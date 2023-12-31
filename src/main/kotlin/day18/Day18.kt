package day18

import java.io.FileReader
import kotlin.math.abs

data class Instruction(
    val direction: String,
    val length: Int
)

data class LongPair(
    val first: Long,
    val second: Long
)

fun generatePoints(instructions: List<Instruction>): List<LongPair> {
    var currentPos = LongPair(0L, 0L)
    val points = mutableListOf(currentPos)
    instructions.forEach { instruction ->
        when (instruction.direction) {
            "U" -> for (i in 1..instruction.length) {
                currentPos = LongPair(currentPos.first, currentPos.second + 1)
                points.add(currentPos)
            }

            "D" -> for (i in 1..instruction.length) {
                currentPos = LongPair(currentPos.first, currentPos.second - 1)
                points.add(currentPos)
            }

            "R" -> for (i in 1..instruction.length) {
                currentPos = LongPair(currentPos.first + 1, currentPos.second)
                points.add(currentPos)
            }

            "L" -> for (i in 1..instruction.length) {
                currentPos = LongPair(currentPos.first - 1, currentPos.second)
                points.add(currentPos)
            }

        }
    }

    return points
}

fun shoelaceFormula(points: List<LongPair>): Double {
    if (points.size < 3) return 0.0

    val n = points.size
    var area = 0.0

    for (i in 0 until n) {
        val j = (i + 1) % n
        area += points[i].first * points[j].second - points[j].first * points[i].second
    }
    return abs(area / 2.0)
}

fun calculateHashes(instructions: List<Instruction>): Long {
    // i + b = A + b/2 + 1
    // A = Shoelace formula

    val points = generatePoints(instructions)
    val b = points.size
    val A = shoelaceFormula(points)
    val i = A - b / 2 + 1

    return (i + b).toLong() - 1
}

fun solve1(instructions: List<Instruction>): Long = calculateHashes(instructions)

fun solve2(instructions: List<Instruction>): Long = calculateHashes(instructions)

fun main() {

    val fr = FileReader("src/main/kotlin/day18/input.txt")
    val input = fr.readLines()

    val instructions = input.map {
        val string = it.split(" ")
        val direction = string[0]
        val length = string[1].toInt()
        Instruction(direction = direction, length = length)
    }

    val newInstructions = input.map {
        val string = it.split(" ")
        val hex = string[2].replace("[()]".toRegex(), "")
        val length = hex.substring(1, hex.length - 1).toInt(radix = 16)
        val direction: String = when (hex.last()) {
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> ""
        }
        Instruction(direction = direction, length = length)
    }

    println("Puzzle 1: " + solve1(instructions))
    println("Puzzle 2: " + solve2(newInstructions))


}