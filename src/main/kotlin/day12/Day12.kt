package day12

import java.io.FileReader


fun generateCombinations(hashCount: Int, dotCount: Int): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val stack = mutableListOf(Triple(hashCount, dotCount, mutableListOf<String>()))

    while (stack.isNotEmpty()) {
        val (remainingHashes, remainingDots, current) = stack.removeAt(stack.size - 1)

        if (remainingHashes == 0 && remainingDots == 0) {
            result.add(ArrayList(current))
            continue
        }

        if (remainingHashes > 0) {
            val newCurrentForHash = ArrayList(current)
            newCurrentForHash.add("#")
            stack.add(Triple(remainingHashes - 1, remainingDots, newCurrentForHash))
        }

        if (remainingDots > 0) {
            val newCurrentForDot = ArrayList(current)
            newCurrentForDot.add(".")
            stack.add(Triple(remainingHashes, remainingDots - 1, newCurrentForDot))
        }
    }

    return result
}

fun generateCombinationsRecursive(
    hashCount: Int,
    dotCount: Int,
    current: MutableList<String>,
    result: MutableList<List<String>>
) {

    if (hashCount == 0 && dotCount == 0) {
        result.add(ArrayList(current))
        return
    }

    if (hashCount > 0) {
        current.add("#")
        generateCombinationsRecursive(hashCount - 1, dotCount, current, result)
        current.removeAt(current.size - 1)
    }

    if (dotCount > 0) {
        current.add(".")
        generateCombinationsRecursive(hashCount, dotCount - 1, current, result)
        current.removeAt(current.size - 1)
    }
}


fun isRowOk(charList: List<String>, intList: List<Int>): Boolean {
    var charIndex = 0

    for (length in intList) {
        var count = 0


        while (charIndex < charList.size && charList[charIndex] != "#") {
            charIndex++
        }

        while (charIndex < charList.size && charList[charIndex] == "#") {
            count++
            charIndex++
        }

        if (count != length) {
            return false
        }
    }
    return true
}


fun <T> replaceElementsAtIndices(
    targetList: MutableList<T>,
    indices: List<Int>,
    replacementList: List<T>
): MutableList<T> {
    if (indices.size != replacementList.size) {
        throw IllegalArgumentException("The size of indices and replacementList must be the same")
    }

    indices.forEachIndexed { index, targetIndex ->
        if (targetIndex in targetList.indices) {
            targetList[targetIndex] = replacementList[index]
        } else {
            throw IndexOutOfBoundsException("Index $targetIndex is out of bounds for the target list")
        }
    }

    return targetList
}

fun checkArrangement(dotsAndHashes: List<String>, damagedGroups: List<Int>): Int {

    var sum = 0
    val indicesToSwap = dotsAndHashes.mapIndexedNotNull { index, s -> if (s == "?") index else null }
    val unknowns = dotsAndHashes.count { it == "?" }
    val sumOfUnknownDamageSprings = damagedGroups.sumOf { it } - dotsAndHashes.count { it == "#" }

    val combinations = generateCombinations(sumOfUnknownDamageSprings, unknowns - sumOfUnknownDamageSprings)

    val okCombinations = combinations.count {
        val testList = replaceElementsAtIndices(dotsAndHashes.toMutableList(), indicesToSwap, it)

        isRowOk(testList, damagedGroups)
    }

    sum += okCombinations

    return sum
}

fun solve1(input: List<String>): Int {

    return input.sumOf { line ->
        val dotsAndHashes = line.split(" ")[0].map { it.toString() }
        val damagedGroups = line.split(" ")[1].split(",").map { it.toInt() }
        checkArrangement(dotsAndHashes, damagedGroups)
    }

}

fun solve2(input: List<String>): Int {

    var currentLine = 1

    val sum = input.sumOf { line ->


        val symbols = line.split(" ")[0]
        val dotsAndHashes = line.split(" ")[0].map { it.toString() }
        val extendedDotsAndHashes = (1..2).joinToString("?") { symbols }.map { it.toString() }


        val numbers = line.split(" ")[1].split(",").map { it.toInt() }
        val damagedGroups = line.split(" ")[1].split(",").map { it.toInt() }
        val extendedDamagedGroups = (1..2).flatMap { numbers }

        val factor = checkArrangement(extendedDotsAndHashes, extendedDamagedGroups) / checkArrangement(
            dotsAndHashes,
            damagedGroups
        )


        val result = checkArrangement(dotsAndHashes, damagedGroups) * factor * factor * factor * factor
        println("line: $currentLine, sum: $result")
        currentLine++
        result
    }

    return sum
}

fun main() {

    val fr = FileReader("src/main/kotlin/day12/input.txt")
    val input = fr.readLines()


    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))

}
