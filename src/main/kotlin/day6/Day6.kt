package day6

import java.io.FileReader

data class Race(
    val time: Int,
    val record: Long
)

fun createRaces(list: List<String>): List<Race> {
    val times = list[0].split(":")[1].trim().split("  +".toRegex()).map { it.toInt() }
    val records = list[1].split(":")[1].trim().split("  +".toRegex()).map { it.toLong() }

    return times.zip(records) { time, distance -> Race(time, distance) }
}

fun calcNumberOfWins(time: Int, record: Long): Int {
    var wins = 0
    var pressButtonTime = time

    for (speed in 0 until time) {
        if (speed.toLong() * pressButtonTime > record) wins++
        pressButtonTime--
    }

    return wins
}

fun solve1(input: List<String>): Int {
    val races = createRaces(input)
    val wins = races.map { calcNumberOfWins(it.time, it.record) }
    var margin = 1
    for (win in wins) margin *= win

    return margin

}

fun solve2(input: List<String>): Int {
    val time = input[0].split(":")[1].trim().replace("  +".toRegex(), "").toInt()
    val record = input[1].split(":")[1].trim().replace("  +".toRegex(), "").toLong()
    val race = Race(time, record)

    return calcNumberOfWins(race.time, race.record)
}

fun main() {

    val fr = FileReader("src/main/kotlin/day6/input.txt")
    val input = fr.readLines()


    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))


}