package day11

import java.io.FileReader
import kotlin.math.abs


typealias Image = MutableList<MutableList<String>>

fun createImage(input: List<String>): Image =
    input.map { row -> row.map { it.toString() }.toMutableList() }.toMutableList()

fun findRowSpaceIndices(image: Image): List<Int> =
    image.mapIndexedNotNull { index, row ->
        if (row.all { it == "." }) index else null
    }

fun findColSpaceIndices(image: Image): List<Int> {
    val numberOfColumns = image[0].size
    return (0 until numberOfColumns).mapNotNull { col ->
        if (image.all { row -> row[col] == "." }) col else null
    }
}

fun expandImage(image: Image, rowIndices: List<Int>, colIndices: List<Int>, amount: Int = 0) {
    var newIndex = 0
    rowIndices.forEach { index ->
        repeat(amount) {
            image.add(index + newIndex, (0..image[0].size).map { "." } as MutableList<String>)
            newIndex++
        }

    }
    newIndex = 0

    colIndices.forEach { index ->
        repeat(amount) {
            image.forEach { row -> row.add(index + newIndex, ".") }
            newIndex ++
        }


    }
}

fun findGalaxies(image: Image): List<Pair<Int, Int>> {
    return image.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, value ->
            if (value == "#") Pair(rowIndex, colIndex) else null
        }
    }
}


fun createGalaxyPairs(indices: List<Pair<Int, Int>>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    return indices.flatMapIndexed { index, firstPair ->
        indices.subList(index + 1, indices.size).map { secondPair ->
            Pair(firstPair, secondPair)
        }
    }
}

fun findDistance(galaxyPair: Pair<Pair<Int, Int>, Pair<Int, Int>>): Int {
    return abs(galaxyPair.first.first - galaxyPair.second.first) + abs(galaxyPair.first.second - galaxyPair.second.second)
}

fun solve1(input: List<String>, expand: Int = 1): Int {
    val image = createImage(input)
    val rowIndices = findRowSpaceIndices(image)
    val colIndices = findColSpaceIndices(image)
    expandImage(image, rowIndices, colIndices, expand)
    val galaxies = findGalaxies(image)
    val pairs = createGalaxyPairs(galaxies)

    return pairs.sumOf { findDistance(it) }
}

fun solve2(input: List<String>): Long {
    val difference = solve1(input, 2) - solve1(input, 1)

    return solve1(input) + (999998L * difference)
}

fun main() {
    val fr = FileReader("src/main/kotlin/day11/input.txt")
    val input = fr.readLines()

    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))


}

