package day13

import java.io.FileReader
import kotlin.math.max

typealias Pattern = List<List<Char>>

// 7522 - Too low
// 23653 - Too low
// 31078 - Too low
enum class Reflection {
    VERTICAL, HORIZONTAL
}

fun createPatterns(input: List<String>): List<Pattern> {
    val patterns: MutableList<Pattern> = mutableListOf()

    var currentPattern: MutableList<MutableList<Char>>? = mutableListOf()

    for (i in input.indices) {
        if (input[i].matches("".toRegex())) {
            patterns.add(currentPattern!!)
            currentPattern = mutableListOf()
        } else {
            val row = input[i].map { it }.toMutableList()
            currentPattern!!.add(row)
        }

    }
    patterns.add(currentPattern!!)

    return patterns
}


fun scanRow(pattern: Pattern): Int {
    var topPointer = 0
    var bottomPointer = pattern.lastIndex
    var reflections1 = 0

    while(bottomPointer != 0) {
        if(pattern[topPointer] == pattern[bottomPointer]) reflections1++
        bottomPointer--
    }


    var reflections2 = 0

    while(topPointer != pattern.lastIndex) {
        if(pattern[topPointer] == pattern[pattern.lastIndex]) reflections2++
        topPointer++
    }

    return max(reflections1,reflections2)

}


fun scanColumn(pattern: Pattern): Int {

    var leftPointer = 0
    var rightPointer = pattern[0].lastIndex
    var reflections1 = 0
    var foundMatch = false

    while(rightPointer >= leftPointer) {
        if(pattern.all { row -> row[leftPointer] == row[rightPointer] }) {
            reflections1++
            foundMatch = true
        }
        if(foundMatch) {
            rightPointer--
            leftPointer++
        } else {
            rightPointer --
        }

    }

    //Reset
    leftPointer = 0
    rightPointer = pattern[0].lastIndex
    foundMatch = false

    var reflections2 = 0

    while(rightPointer <= leftPointer) {
        if(pattern.all { row -> row[leftPointer] == row[rightPointer] }) {
            reflections2++
            foundMatch = true
        }
        if(foundMatch) {
            rightPointer++
            leftPointer--
        } else {
            rightPointer ++
        }

    }

    return max(reflections1,reflections2)

}

fun findHorizontalIndex(pattern: Pattern): Pair<Int, Reflection>? {
    for (i in 0 until pattern.size - 1) {
        if (pattern[i] == pattern[i + 1]) {
            return Pair(i, Reflection.HORIZONTAL)
        }
    }

    return null
}

fun findVerticalIndex(pattern: Pattern): Pair<Int, Reflection>? {
    for (j in 0 until pattern[0].size - 1) {
        if (pattern.all { row -> row[j] == row[j + 1] }) {
            return Pair(j, Reflection.VERTICAL)
        }
    }

    return null
}

fun findReflectionIndex(pattern: Pattern): Int {
    val horizontalIndex = findHorizontalIndex(pattern)
    val verticalIndex = findVerticalIndex(pattern)

    // Backtrack horizontally
    var horizontalScore = 0

    if (horizontalIndex != null) {
        var startPointer = horizontalIndex.first
        var endPointer = horizontalIndex.first + 1

        try {
            while (pattern[startPointer] == pattern[endPointer]) {
                horizontalScore++
                startPointer--
                endPointer++
            }
        } catch (_: IndexOutOfBoundsException) {
        }

    }


    // Backtrack vertically
    var verticalScore = 0

    if (verticalIndex != null) {

        var startPointer = verticalIndex.first
        var endPointer = verticalIndex.first + 1

        try {
            while (pattern.all { row -> row[startPointer] == row[endPointer] }) {
                verticalScore++
                startPointer--
                endPointer++
            }
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    println("Vertical score: $verticalScore - Horizontalscore: $horizontalScore")

    return if (verticalScore > horizontalScore) verticalIndex!!.first + 1
    else (horizontalIndex!!.first + 1) * 100

}

fun solve1(patterns: List<Pattern>): Int {

    var pattern = 1

    return patterns.sumOf {
        println("Pattern ${pattern++}")
        findReflectionIndex(it)
    }
}

fun solve2(patterns: List<Pattern>): Int {

    patterns.forEach {
        println(scanColumn(it))
    }

    return 0
}

fun main() {

    val fr = FileReader("src/main/kotlin/day13/input.txt")
    val input = fr.readLines()

    val patterns = createPatterns(input)

 //   println("Puzzle 1: " + solve1(patterns))
    println("Puzzle 2: " + solve2(patterns))

}