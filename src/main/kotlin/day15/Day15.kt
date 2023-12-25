package day15

import java.io.FileReader

data class Lens(val id: String, val power: Int) {
    override fun equals(other: Any?): Boolean {
        other as Lens
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

fun hashAlgo(string: String) = string.fold(0) { sum, char -> ((sum + char.code) * 17) % 256 }
fun solve1(sequence: List<String>): Int = sequence.sumOf { string -> hashAlgo(string) }

fun solve2(sequence: List<String>): Int {

    val boxMap: HashMap<Int, MutableList<Lens>> = hashMapOf()

    (0..255).forEach { boxMap[it] = mutableListOf() }

    sequence.forEach { string ->
        val command = string.split("[=-]".toRegex()).filter { it.isNotEmpty() }

        val addLens = command.size == 2
        val key = hashAlgo(command.first())

        if (addLens) {
            val newLens = Lens(id = command.first(), power = command.last().toInt())
            val index = boxMap[key]!!.indexOf(newLens)

            if (index < 0) {
                boxMap[key]!!.add(newLens)
            } else {
                boxMap[key]!!.removeAt(index)
                boxMap[key]!!.add(index, newLens)
            }

        } else {
            val box = boxMap[key]!!
            box.removeIf { command.first() == it.id }
        }
    }

    var sum = 0

    boxMap.forEach { (box, lenses) ->
        lenses.forEachIndexed { index, lens ->
            sum += (box + 1) * (index + 1) * lens.power
        }
    }

    return sum
}

fun main() {

    val fr = FileReader("src/main/kotlin/day15/input.txt")
    val input = fr.readLines()
    val sequence = input.first().split(",")


    println("Puzzle 1: " + solve1(sequence))
    println("Puzzle 2: " + solve2(sequence))


}