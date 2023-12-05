package day4

import java.io.FileReader
import kotlin.math.pow


data class Card(
    val id: Int,
    val myNumbers: List<Int>,
    val winningNumbers: List<Int>
)

fun createCard(input: String): Card {
    val serializedString = input.replace("  +".toRegex(), " ")
    val cardID = serializedString.split(":")[0].split(" ")[1].toInt()
    val numbers = serializedString.split(":")[1]

    val winningNumbers = numbers.split("|")[0].trim().split(" ").map { number -> number.toInt() }
    val myNumbers = numbers.split("|")[1].trim().split(" ").map { number -> number.toInt() }

    return Card(id = cardID, winningNumbers = winningNumbers, myNumbers = myNumbers)
}

fun calculatePoints(winningNumbers: Int) =
    if (winningNumbers <= 0) 0 else 2.0.pow((winningNumbers - 1).toDouble()).toInt()

fun calculateCopies(card: Card) = card.myNumbers.filter { myNumber -> card.winningNumbers.contains(myNumber) }.size

fun solve1(list: List<String>): Int {
    val totalPoints = list.map { createCard(it) }
        .sumOf { card ->
            val matches = card.myNumbers.filter { myNumber -> card.winningNumbers.contains(myNumber) }
            calculatePoints(matches.size)

        }

    return totalPoints
}

fun solve2(list: List<String>): Int {
    val cards = list.map { createCard(it) }.map { card -> Pair(card.id, calculateCopies(card)) }.toMutableList()
    var index = 0

    while (index < cards.size) {
        val copies = cards.subList(cards[index].first, cards[index].first + cards[index].second)
        cards.addAll(copies)

        index++
    }

    return cards.size
}

fun main() {

    val fr = FileReader("src/main/kotlin/day4/input.txt")
    val input = fr.readLines()

    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))

}