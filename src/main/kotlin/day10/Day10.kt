package day10

import java.io.FileReader

typealias Maze = Array<Array<String>>

enum class Direction { UP, DOWN, LEFT, RIGHT, DONE, STOP }

fun createMaze(input: List<String>): Maze {
    return input.map { row -> row.map { it.toString() }.toTypedArray() }.toTypedArray()
}

fun getStartPoint(maze: Maze) = maze.flatMapIndexed { rowIndex, row ->
    row.mapIndexed { colIndex, value -> if (value == "S") Pair(rowIndex, colIndex) else null }
}.firstOrNull { it != null }

fun getPipeDirection(direction: String): Pair<Direction, Direction> {
    return when (direction) {
        "|" -> Pair(Direction.UP, Direction.DOWN)
        "-" -> Pair(Direction.LEFT, Direction.RIGHT)
        "L" -> Pair(Direction.UP, Direction.RIGHT)
        "J" -> Pair(Direction.UP, Direction.LEFT)
        "7" -> Pair(Direction.LEFT, Direction.DOWN)
        "F" -> Pair(Direction.RIGHT, Direction.DOWN)
        "." -> Pair(Direction.STOP, Direction.STOP)
        "S" -> Pair(Direction.DONE, Direction.DONE)
        else -> throw IllegalArgumentException("Unknown pipe!")
    }
}

fun getNextPoint(direction: Direction, point: Pair<Int, Int>): Pair<Int, Int> {
    return when (direction) {
        Direction.UP -> Pair(point.first - 1, point.second)
        Direction.DOWN -> Pair(point.first + 1, point.second)
        Direction.LEFT -> Pair(point.first, point.second - 1)
        Direction.RIGHT -> Pair(point.first, point.second + 1)
        Direction.DONE -> Pair(point.first, point.second)
        Direction.STOP -> Pair(point.first, point.second)
    }
}

fun getStartDirections(maze: Maze, row: Int, col: Int): Pair<Direction, Direction> {
    val upSafe = row - 1 >= 0
    val downSafe = row + 1 < maze.size
    val rightSafe = col + 1 < maze[maze.lastIndex].lastIndex
    val leftSafe = col - 1 >= 0
    val directions = mutableListOf<Direction>()
    if (upSafe && maze[row - 1][col].matches("(7|F|\\|)".toRegex())) directions.add(Direction.UP)
    if (downSafe && maze[row + 1][col].matches("(L|J|\\|)".toRegex())) directions.add(Direction.DOWN)
    if (rightSafe && maze[row][col + 1].matches("(7|J|-)".toRegex())) directions.add(Direction.RIGHT)
    if (leftSafe && maze[row][col - 1].matches("(L|F|-)".toRegex())) directions.add(Direction.LEFT)
    assert(directions.size == 2)
    return Pair(directions.first(), directions.last())
}



fun solve1(input: List<String>): Int {
    val maze = createMaze(input)
    var currentPoint = getStartPoint(maze)!!
    var steps = 0
    val visited = mutableSetOf<Pair<Int, Int>>()

    while (true) {
        visited.add(currentPoint)
        val pipeDirections =
            if (steps == 0) getStartDirections(maze, currentPoint.first, currentPoint.second)
             else getPipeDirection(maze[currentPoint.first][currentPoint.second])

        currentPoint = if (!visited.contains(getNextPoint(pipeDirections.first, currentPoint))) {
            getNextPoint(pipeDirections.first, currentPoint)
        } else if (!visited.contains(getNextPoint(pipeDirections.second, currentPoint))) {
            getNextPoint(pipeDirections.second, currentPoint)
        } else {
            steps++
            break
        }
        steps++

    }

    return steps / 2
}

fun solve2(input: List<String>): Int {

    return 0
}

fun main() {
    val fr = FileReader("src/main/kotlin/day10/input.txt")
    val input = fr.readLines()

    println("Puzzle 1: " + solve1(input))
    println("Puzzle 2: " + solve2(input))

}
