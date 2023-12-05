package day3

import java.io.FileReader


fun main() {

    val fr = FileReader("src/main/kotlin/day3/input.txt")
    val matrix = fr.readLines().map { it.map { char -> char }.toTypedArray() }.toTypedArray()

    // [*, %, -, #, =, @, $, /, +, &]

    println("Puzzle 1: ${solve1(matrix)}")
    println("Puzzle 2: ${solve2(matrix)}")
}

fun Char.isSpecificSymbol(regex: Regex? = null) = this.toString().matches(regex?: "[*%\\-#=@$/+&]".toRegex())

fun checkUpDownLeftRight(elemRow: Int, elemCol: Int, matrix: Array<Array<Char>>, regex: Regex? = null): Boolean {
    val safeRight = elemCol + 1 < matrix[elemRow].size
    val safeLeft = elemCol - 1 >= 0
    val safeTop = elemRow - 1 >= 0
    val safeDown = elemRow + 1 < matrix.size

    val right = safeRight && matrix[elemRow][elemCol + 1].isSpecificSymbol(regex)
    val left = safeLeft && matrix[elemRow][elemCol - 1].isSpecificSymbol(regex)
    val up = safeTop && matrix[elemRow - 1][elemCol].isSpecificSymbol(regex)
    val down = safeDown && matrix[elemRow + 1][elemCol].isSpecificSymbol(regex)

    return right || left || up || down
}

fun checkDiagonally(elemRow: Int, elemCol: Int, matrix: Array<Array<Char>>, regex: Regex? = null): Boolean {
    val safeRight = elemCol + 1 < matrix[elemRow].size
    val safeLeft = elemCol - 1 >= 0
    val safeTop = elemRow - 1 >= 0
    val safeDown = elemRow + 1 < matrix.size

    return (safeRight && safeTop && matrix[elemRow - 1][elemCol + 1].isSpecificSymbol(regex)) ||
            (safeRight && safeDown && matrix[elemRow + 1][elemCol + 1].isSpecificSymbol(regex)) ||
            (safeLeft && safeTop && matrix[elemRow - 1][elemCol - 1].isSpecificSymbol(regex)) ||
            (safeLeft && safeDown && matrix[elemRow + 1][elemCol - 1].isSpecificSymbol(regex))
}

fun checkAdjacent(elemRow: Int, elemCol: Int, matrix: Array<Array<Char>>,regex: Regex? = null): Boolean {
    return checkUpDownLeftRight(elemRow, elemCol, matrix, regex) || checkDiagonally(elemRow, elemCol, matrix, regex)
}

fun checkIsGear(elemRow: Int, elemCol: Int, matrix: Array<Array<Char>>): Pair<Int, Int>? {
    val safeTop = elemRow - 1 >= 0
    val safeRight = elemCol + 1 < matrix[elemRow].size
    val safeDown = elemRow + 1 < matrix.size
    val safeLeft = elemCol - 1 >= 0
    val safeTopRight = safeTop && safeRight
    val safeBottomRight = safeRight && safeDown
    val safeBottomLeft = safeLeft && safeDown
    val safeTopLeft = safeTop && safeLeft

    return when {
        safeTop && matrix[elemRow - 1][elemCol] == '*' -> Pair(elemRow - 1, elemCol)
        safeTopRight && matrix[elemRow - 1][elemCol + 1] == '*' -> Pair(elemRow - 1, elemCol + 1)
        safeRight && matrix[elemRow][elemCol + 1] == '*' -> Pair(elemRow, elemCol + 1)
        safeBottomRight && matrix[elemRow + 1][elemCol + 1] == '*' -> Pair(elemRow + 1, elemCol + 1)
        safeDown && matrix[elemRow + 1][elemCol] == '*' -> Pair(elemRow + 1, elemCol)
        safeBottomLeft && matrix[elemRow + 1][elemCol - 1] == '*' -> Pair(elemRow + 1, elemCol - 1)
        safeLeft && matrix[elemRow][elemCol - 1] == '*' -> Pair(elemRow, elemCol - 1)
        safeTopLeft && matrix[elemRow - 1][elemCol - 1] == '*' -> Pair(elemRow - 1, elemCol - 1)
        else -> null
    }
}

data class Group(
    val id: Int,
    val startIndex: Pair<Int, Int>,
    val numbers: MutableList<Char> = mutableListOf()
) {
    fun getGroup() = numbers.joinToString(separator = "").toInt()
}

fun createGroups(matrix: Array<Array<Char>>): List<Group> {
    val groups = mutableListOf<Group>()

    for (row in matrix.indices) {
        var newGroup: Group? = null

        for (col in matrix[row].indices) {
            val current = matrix[row][col]

            if (current.isDigit()) {
                if (newGroup == null) {
                    newGroup = Group(id = groups.size, startIndex = Pair(row, col))
                    groups.add(newGroup)
                }
                newGroup.numbers.add(current)
            } else {
                newGroup = null
            }
        }
    }

    return groups
}


fun solve1(matrix: Array<Array<Char>>): Int {
    val groups = createGroups(matrix)

    val validGroups = groups.filter { group ->
        group.numbers.withIndex().any { (i, _) ->
            checkAdjacent(group.startIndex.first, group.startIndex.second + i, matrix)
        }
    }

    return validGroups.sumOf { it.getGroup() }
}

fun solve2(matrix: Array<Array<Char>>): Int {
    val groups = createGroups(matrix)

    val validGroups = groups.filter { group ->
        group.numbers.withIndex().any { (i, _) ->
            checkAdjacent(group.startIndex.first, group.startIndex.second + i, matrix, "\\*".toRegex())
        }
    }

    val gearGroup = validGroups.flatMap { group ->
        group.numbers.withIndex().mapNotNull { (i, _) ->
            val coord = checkIsGear(group.startIndex.first, group.startIndex.second + i, matrix)
            coord?.let { it to group }
        }
    }.groupBy(
        keySelector = { it.first },
        valueTransform = { it.second }
    ).mapValues { (_, groups) ->
        groups.distinct()
    }

    val validGearGroups = gearGroup.filter { it.value.size == 2 }

    val totalSum = validGearGroups.values.sumOf { group ->
        group.fold(1) { product, element -> product * element.getGroup() }.toInt()
    }

    return totalSum
}