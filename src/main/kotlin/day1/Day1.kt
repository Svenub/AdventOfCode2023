package day1

import java.io.FileReader


fun main() {

    val fr = FileReader("src/main/kotlin/day1/input.txt")
    val listOfCharLists = fr.readLines().map { it.map { char -> char.toString() } }
    var result1 = 0
    var result2 = 0
    listOfCharLists.forEach { line ->
        result1 += solve1(line)
        result2 += solve2(line)
    }
    println(result1)
    println(result2)
}


fun String.textToDigit(): String {
    return when {
        this == "one" -> "1"
        this == "two" -> "2"
        this == "three" -> "3"
        this == "four" -> "4"
        this == "five" -> "5"
        this == "six" -> "6"
        this == "seven" -> "7"
        this == "eight" -> "8"
        this == "nine" -> "9"
        else -> "NaN"
    }
}

fun solve1(input: List<String>): Int {
    var result = ""

    for (i in input.indices) {
        val char = input[i].toCharArray()[0]
        if (char.isDigit()) {
            result += char
            break
        }
    }

    for (i in input.size - 1 downTo 0) {
        val char = input[i].toCharArray()[0]
        if (char.isDigit()) {
            result += char
            break
        }
    }

    return result.toInt()
}

fun solve2(input: List<String>): Int {
    val pattern = "(one|two|three|four|five|six|seven|eight|nine)".toRegex()

    var result = ""
    var acc = ""

    for (i in input.indices) {
        val char = input[i].toCharArray()[0]
        if (char.isDigit()) {
            result += char
            break
        } else {
            acc += char
            val matchResult = pattern.find(acc)
            if (matchResult != null) {
                result += matchResult.value.textToDigit()
                acc = ""
                break
            }
        }
    }


    for (i in input.size - 1 downTo 0) {
        val char = input[i].toCharArray()[0]
        if (char.isDigit()) {
            result += char
            break
        } else {
            acc = char + acc
            val matchResult = pattern.find(acc)
            if (matchResult != null) {
                result += matchResult.value.textToDigit()
                break
            }
        }
    }


    return result.toInt()
}
