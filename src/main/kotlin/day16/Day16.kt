package day16

import day16.Direction.*
import java.io.FileReader

enum class Direction { NORTH, EAST, SOUTH, WEST }

fun isVertical(direction: Direction): Boolean = direction == NORTH || direction == SOUTH

fun isHorizontal(direction: Direction): Boolean = direction == EAST || direction == WEST


data class Beam(
    var xPos: Int,
    var yPos: Int,
    var direction: Direction
) {
    fun addPositionToHistory(): Boolean {
        return history.add(Triple(xPos, yPos, direction))
    }
    fun clearHistory() = history.clear()

    companion object {
        val history: MutableSet<Triple<Int, Int, Direction>> = mutableSetOf()
    }
}

fun goForward(beam: Beam) {
    when (beam.direction) {
        NORTH -> beam.yPos--
        EAST -> beam.xPos++
        SOUTH -> beam.yPos++
        WEST -> beam.xPos--
    }
}

fun changeDirection(currentChar: Char, currentBeam: Beam, allBeams: MutableList<Beam>) {

    when (currentChar) {
        '|' -> if (isHorizontal(currentBeam.direction)) {
            allBeams.add(Beam(currentBeam.xPos, currentBeam.yPos, NORTH))
            currentBeam.direction = SOUTH
        }

        '-' -> if (isVertical(currentBeam.direction)) {
            allBeams.add(Beam(currentBeam.xPos, currentBeam.yPos, WEST))
            currentBeam.direction = EAST
        }

        '/' ->
            when (currentBeam.direction) {
                NORTH -> currentBeam.direction = EAST
                EAST -> currentBeam.direction = NORTH
                SOUTH -> currentBeam.direction = WEST
                WEST -> currentBeam.direction = SOUTH
            }

        '\\' ->
            when (currentBeam.direction) {
                NORTH -> currentBeam.direction = WEST
                EAST -> currentBeam.direction = SOUTH
                SOUTH -> currentBeam.direction = EAST
                WEST -> currentBeam.direction = NORTH
            }
    }
}


fun isBeamInside(beam: Beam, matrix: List<List<Char>>): Boolean {
    return beam.xPos >= 0 && beam.xPos < matrix[0].size && beam.yPos >= 0 && beam.yPos < matrix.size
}

fun getEnergizedTiles(xPos: Int, yPos: Int, direction: Direction, matrix: List<List<Char>>): Int {
    val allBeams = mutableListOf<Beam>()
    val energizedTiles = mutableSetOf<Pair<Int, Int>>()

    val startBeam = Beam(xPos = xPos, yPos = yPos, direction)
    allBeams.add(startBeam)
    startBeam.addPositionToHistory()
    energizedTiles.add(Pair(startBeam.xPos, startBeam.yPos))
    changeDirection(matrix[startBeam.yPos][startBeam.xPos], startBeam, allBeams)

    while (allBeams.isNotEmpty()) {
        var index = 0

        while (index < allBeams.size) {
            val currentBeam = allBeams[index]

            goForward(currentBeam)

            if (isBeamInside(currentBeam, matrix) && currentBeam.addPositionToHistory()) {

                energizedTiles.add(Pair(currentBeam.xPos, currentBeam.yPos))
                val currentPlace = matrix[currentBeam.yPos][currentBeam.xPos]

                changeDirection(currentPlace, currentBeam, allBeams)

            } else
                allBeams.remove(currentBeam)

            index++
        }

    }
    startBeam.clearHistory()
    return energizedTiles.size
}

fun solve1(matrix: List<List<Char>>): Int {

    return getEnergizedTiles(0, 0, direction = EAST, matrix)
}

fun solve2(matrix: List<List<Char>>): Int {

    val rows = matrix.size
    val cols = matrix[0].size

    var maxEnergized = 0

    // Top row, beam heading downward
    for (x in 0 until cols) {
        maxEnergized = maxOf(maxEnergized, getEnergizedTiles(x, 0, SOUTH, matrix))
    }

    // Bottom row, beam heading upward
    for (x in 0 until cols) {
        maxEnergized = maxOf(maxEnergized, getEnergizedTiles(x, rows - 1, NORTH, matrix))
    }

    // Leftmost column, beam heading right
    for (y in 0 until rows) {
        maxEnergized = maxOf(maxEnergized, getEnergizedTiles(0, y, EAST, matrix))
    }

    // Rightmost column, beam heading left
    for (y in 0 until rows) {
        maxEnergized = maxOf(maxEnergized, getEnergizedTiles(cols - 1, y, WEST, matrix))
    }

    return maxEnergized
}


fun main() {

    val fr = FileReader("src/main/kotlin/day16/input.txt")
    val input = fr.readLines()

    val matrix = input.map { row -> row.map { it } }

    println("Puzzle 1: " + solve1(matrix))
    println("Puzzle 2: " + solve2(matrix))


}