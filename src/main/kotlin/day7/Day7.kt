package day7

import java.io.FileReader

// 249540325 - too low
// 249806423
// 249927655 - Not right
// 249400220

enum class Card(val rankValue: Int) {
    ACE(14), KING(13), QUEEN(12), JACK(11), TEN(10), NINE(9),
    EIGHT(8), SEVEN(7), SIX(6), FIVE(5), FOUR(4), THREE(3), TWO(2), JOKER(1)
}

enum class HandType(val type: Int) {
    FiveOfAKind(7), FourOfAKind(6), FullHouse(5), ThreeOfAKind(4), TwoPair(3), OnePair(2), HighCard(1)
}

data class Hand(
    val cards: List<Card>,
    val handType: HandType,
    val bid: Int
)

fun getCardType(letter: String, withJoker: Boolean): Card {
    return when (letter) {
        "A" -> Card.ACE
        "K" -> Card.KING
        "Q" -> Card.QUEEN
        "J" -> if (withJoker) Card.JOKER else Card.JACK
        "T" -> Card.TEN
        else ->
            Card.values().find { it.rankValue == letter.toInt() }
                ?: throw (IllegalArgumentException("Unknown card type"))
    }
}

fun getHandType(letters: List<String>, withJoker: Boolean): HandType {
    val groups = letters.groupBy { it }
    val threeOfaKind = groups.any { it.value.size == 3 }
    val fourOfaKind = groups.any { it.value.size == 4 }

    return if (withJoker && groups.any { it.key == "J" }) {
        val fourJokers = groups.any { group -> group.key == "J" && group.value.size == 4 }
        val threeJokers = groups.any { group -> group.key == "J" && group.value.size == 3 }
        val twoJokers = groups.any { group -> group.key == "J" && group.value.size == 2 }
        val oneJoker = groups.any { group -> group.key == "J" && group.value.size == 1 }

        when (groups.keys.size) {
            5 -> HandType.HighCard
            4 -> if (oneJoker) HandType.ThreeOfAKind else HandType.OnePair
            3 -> {
                if (oneJoker)
                    if (threeOfaKind) HandType.FourOfAKind
                    else HandType.FullHouse
                else if (twoJokers) HandType.FourOfAKind
                else HandType.ThreeOfAKind
            }
            2 -> if (fourJokers) HandType.FourOfAKind else HandType.FiveOfAKind
            1 -> HandType.FiveOfAKind
            else -> throw IllegalArgumentException("Unknown hand size")
        }
    } else {
        when (groups.keys.size) {
            5 -> HandType.HighCard
            4 -> HandType.OnePair
            3 -> if (threeOfaKind) HandType.ThreeOfAKind else HandType.TwoPair
            2 -> if (fourOfaKind) HandType.FourOfAKind else HandType.FullHouse
            1 -> HandType.FiveOfAKind
            else -> throw IllegalArgumentException("Unknown hand size")
        }
    }
}


fun createHand(input: String, withJoker: Boolean): Hand {
    val letters = input.split(" ")[0].map { it.toString() }
    val bid = input.split(" ")[1].toInt()

    return Hand(
        cards = letters.map { getCardType(it, withJoker) },
        handType = getHandType(letters, withJoker),
        bid = bid
    )

}


fun solve1(hands: List<Hand>): Int {
    val sortedHands = hands.sortedWith(
        compareBy(
            { it.handType.type },
            { it.cards[0].rankValue },
            { it.cards[1].rankValue },
            { it.cards[2].rankValue },
            { it.cards[3].rankValue },
            { it.cards[4].rankValue }
        ))

    var sum = 0
    sortedHands.withIndex().forEach { hand ->
        sum += hand.value.bid * (hand.index + 1)
    }

    return sum

}

fun solve2(hands: List<Hand>): Int {
    val sortedHands = hands.sortedWith(
        compareBy(
            { it.handType.type },
            { it.cards[0].rankValue },
            { it.cards[1].rankValue },
            { it.cards[2].rankValue },
            { it.cards[3].rankValue },
            { it.cards[4].rankValue }
        ))

    var sum = 0
    sortedHands.withIndex().forEach { hand ->

        println("hand: $hand ")
        sum += hand.value.bid * (hand.index + 1)
    }

    sortedHands.filter { hand -> hand.cards.any { card -> card == Card.JOKER } }.forEach { hand ->

    }

    return sum

}

fun main() {

    val fr = FileReader("src/main/kotlin/day7/input.txt")
    val input = fr.readLines()


    println("Puzzle 1: " + solve1(input.map { createHand(it, false) }))
    println("Puzzle 2: " + solve2(input.map { createHand(it, true) }))
}