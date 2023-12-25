package day14

import java.io.FileReader


fun Char.isCube() = this == '#'
fun Char.isRoundRock() = this == 'O'

enum class Direction { NORTH, EAST, SOUTH, WEST }

fun tiltPlatform(platform: MutableList<MutableList<Char>>, direction: Direction = Direction.NORTH) {
    when (direction) {
        Direction.NORTH -> {
            for (i in platform.indices) {
                var currentStop = 0
                for (j in platform[i].indices) {

                    if (platform[j][i].isCube()) {
                        currentStop = j + 1
                    }
                    if (platform[j][i].isRoundRock()) {
                        if (j != currentStop && currentStop + 1 < platform[j].size) {
                            platform[currentStop][i] = 'O'
                            platform[j][i] = '.'
                        }
                        currentStop++

                    }

                }
            }
        }

        Direction.EAST -> {
            for (i in platform.indices) {
                var currentStop = platform[i].lastIndex
                for (j in platform[i].lastIndex downTo 0) {

                    if (platform[i][j].isCube()) {
                        currentStop = j - 1
                    }
                    if (platform[i][j].isRoundRock()) {
                        if (j != currentStop) {
                            platform[i][currentStop] = 'O'
                            platform[i][j] = '.'
                        }
                        currentStop--

                    }

                }
            }
        }

        Direction.SOUTH -> {

            for (i in platform.indices) {
                var currentStop = platform[i].lastIndex
                for (j in platform[i].lastIndex downTo 0) {

                    if (platform[j][i].isCube()) {
                        currentStop = j - 1
                    }
                    if (platform[j][i].isRoundRock()) {
                        if (j != currentStop && currentStop - 1 >= 0) {
                            platform[currentStop][i] = 'O'
                            platform[j][i] = '.'
                        }
                        currentStop--

                    }

                }
            }
        }

        Direction.WEST -> {
            for (i in platform.indices) {
                var currentStop = 0
                for (j in platform[i].indices) {

                    if (platform[i][j].isCube()) {
                        currentStop = j + 1
                    }
                    if (platform[i][j].isRoundRock()) {
                        if (j != currentStop && currentStop + 1 < platform[i].size) {
                            platform[i][currentStop] = 'O'
                            platform[i][j] = '.'
                        }
                        currentStop++
                    }
                }
            }
        }
    }
}

fun totalLoad(platform: MutableList<MutableList<Char>>): Int {
    var sum = 0
    for (i in platform.indices) {
        var rocks = 0
        for (j in platform[i].indices) {
            if (platform[i][j].isRoundRock()) rocks++
        }
        sum += rocks * (platform.size - i)
    }

    return sum
}

fun areMatricesEqual(matrix1: List<List<Any>>, matrix2: List<List<Any>>): Boolean {
    if (matrix1.size != matrix2.size) return false

    for (i in matrix1.indices) {
        val row1 = matrix1[i]
        val row2 = matrix2[i]

        if (row1.size != row2.size) return false

        for (j in row1.indices) {
            if (row1[j] != row2[j]) return false
        }
    }

    return true
}

fun solve1(platform: MutableList<MutableList<Char>>): Int {

    tiltPlatform(platform, Direction.NORTH)

    return totalLoad(platform)
}


fun solve2(platform: MutableList<MutableList<Char>>): Int {

    for (i in 0 until 10000) {
        tiltPlatform(platform, Direction.NORTH)
        tiltPlatform(platform, Direction.WEST)
        tiltPlatform(platform, Direction.SOUTH)
        tiltPlatform(platform, Direction.EAST)
    }

    return totalLoad(platform)
}

fun main() {

    val fr = FileReader("src/main/kotlin/day14/input.txt")
    val input = fr.readLines()

    val matrix = input.map { it -> it.map { it }.toMutableList() }.toMutableList()

    println("Puzzle 1: " + solve1(matrix))
    println("Puzzle 2: " + solve2(matrix))


}