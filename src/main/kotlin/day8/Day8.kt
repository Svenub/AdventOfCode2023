package day8

import java.io.FileReader


data class Path(
    val source: String,
    val left: String,
    val right: String,
)

data class IndexPath(
    val source: String,
    val left: Int,
    val right: Int,
    val start: Boolean,
    val end: Boolean
)

fun createIndexPaths(paths: List<Path>): List<IndexPath> {
    val sourceToIndexMap = paths.mapIndexed { index, path -> path.source to index }.toMap()

    return paths.map { path ->
        val leftIndex = sourceToIndexMap[path.left] ?: -1
        val rightIndex = sourceToIndexMap[path.right] ?: -1
        val isStart = path.source.last() == 'A'
        val isEnd = path.source.last() == 'Z'

        IndexPath(path.source, leftIndex, rightIndex, isStart, isEnd)
    }
}

fun steps(instructions: List<String>, paths: List<IndexPath>, startPath: IndexPath): Int {
    var index = 0
    var currentPath = startPath
    var steps = 0

    while (!currentPath.end) {
        when (instructions[index]) {
            "L" -> currentPath = paths[currentPath.left]
            "R" -> currentPath = paths[currentPath.right]
        }
        index++
        steps++
        if (index == instructions.size) index = 0
    }

    return steps
}


fun solve1(instructions: List<String>, paths: List<IndexPath>): Int {
    val startPath = paths.first { it.start }

    return steps(instructions, paths, startPath)
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return kotlin.math.abs(a * b) / gcd(a, b)
}

fun lcmOfList(numbers: List<Long>): Long {
    return numbers.reduce { acc, num -> lcm(acc, num) }
}


fun solve2(instructions: List<String>, paths: List<IndexPath>): Long {

    val startingPaths = paths.filter { it.start }.toMutableList()
    val cycles = startingPaths.map { path ->
        steps(instructions, paths, path).toLong()
    }

    return lcmOfList(cycles)
}

fun main() {
    val fr = FileReader("src/main/kotlin/day8/input.txt")
    val input = fr.readLines()

    val instructions = input[0].map { it.toString() }

    val paths = input.subList(2, input.size).map { line ->
        val (source, destination) = line.split("=")
        val (left, right) = destination.replace("[()]".toRegex(), "").trim().split(",")
        Path(source.trim(), left.trim(), right.trim())
    }

    val indexPaths = createIndexPaths(paths)

    println("Puzzle 1: " + solve1(instructions, indexPaths))
    println("Puzzle 2: " + solve2(instructions, indexPaths))

}