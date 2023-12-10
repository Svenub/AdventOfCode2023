package day9

import java.io.FileReader

typealias History = MutableList<MutableList<Int>>

fun createSequence(list: List<Int>): List<Int> = list.zipWithNext { a, b -> b - a }

fun sequenceDone(list: List<Int>): Boolean = list.all { it == 0 }

fun createHistory(list: List<Int>): History {
    var currentSequence = list.toMutableList()
    val history = mutableListOf<MutableList<Int>>()

    while (!sequenceDone(currentSequence)) {
        history.add(currentSequence)
        currentSequence = createSequence(currentSequence).toMutableList()

    }
    history.add(currentSequence)
    return history
}



fun solve1(input: List<String>): Int {
    val inputs = input.map { it -> it.split(" ").map { it.toInt() } }
    val histories = inputs.map { line -> createHistory(line) }
    return histories.sumOf { history -> history.sumOf { it.last() } }
}


fun solve2(input: List<String>): Int {
    val inputs = input.map { it -> it.split(" ").map { it.toInt() } }
    val histories = inputs.map { line -> createHistory(line) }

    val sum = histories.sumOf { history ->
        var start = history.indices.first
        for(i in history.indices.last - 1 downTo 0) {
            val a =  history[i].first() - start
            start = a
        }
        start
    }

    return sum
}

fun main() {
    val fr = FileReader("src/main/kotlin/day9/input.txt")
    val input = fr.readLines()

    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))

}
