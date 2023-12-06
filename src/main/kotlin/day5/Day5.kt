package day5

import java.io.FileReader
import kotlin.system.measureTimeMillis

typealias Seed = Long
typealias SeedRange = Pair<Long, Long>

data class Map(
    val sourceStart: Long,
    val toDestinationStart: Long,
    val range: Long,
) {
    fun isInRange(source: Long): Boolean = sourceStart <= source && source <= sourceStart + (range - 1)

    fun getDestination(source: Long): Long {
        return when (isInRange(source)) {
            false -> source
            true -> toDestinationStart + (source - sourceStart)
        }
    }

}

fun createSeeds(input: String): List<Seed> {
    return input.split(":")[1].trim().split(" ").map { it.toLong() }
}

fun createSeedRange(input: String): List<SeedRange> {
    val string = input.split(":")[1].trim().split(" ").map { it.toLong() }
    val seeds = mutableListOf<SeedRange>()
    for((index, value) in string.withIndex()) {
        if (index % 2 == 0) {
            seeds.add(Pair(value, string[index + 1]))
        }
    }
    return seeds
}

fun createMoreSeeds(input: String, startFrom: Int): List<Seed> {
    val seeds = mutableListOf<Seed>()
    val string = input.split(":")[1].trim().split(" ").map { it.toLong() }
    val list = string.subList(startFrom, startFrom + 2)
    val startRange = list[0]
    val range = list[1]

    for (index in 0 until range) {
        seeds.add(startRange + index)
        println("added: " + (startRange + index))
    }


    return seeds
}

fun createMap(input: String): Map {
    val numbers = input.split(" ")
    return Map(numbers[1].toLong(), numbers[0].toLong(), numbers[2].toLong())
}


fun goToSource(source: Long, map: List<Map>): Long {
    val sources = map.firstOrNull { it.isInRange(source) }

    return sources?.getDestination(source) ?: source
}
/*
fun goToSourceFromRange(seedRange: SeedRange, map: List<Map>): Long {
    val mapMinRange = map.first().sourceStart
    val mapMaxRange = map.last().sourceStart + (map.last().range - 1)
    val inRange = mapMinRange <= seedRange.first && (seedRange.first + seedRange.second - 1) <= mapMaxRange

    if(mapMinRange <= seedRange.first) {

    }

    for (index in 0 until range) {
        seeds.add(startRange + index)
        println("added: " + (startRange + index))
    }

    return if(inRange) map.firstOrNull{it.isInRange()}



}

 */

fun getLowestLocation(
    seeds: List<Seed>,
    input: List<String>,
): Long {

    val seedToSoilMap = input.subList(3, 26).map { createMap(it) }.sortedBy { it.sourceStart }
    val soilToFertilizerMap = input.subList(28, 59).map { createMap(it) }.sortedBy { it.sourceStart }
    val fertilizerToWaterMap = input.subList(61, 87).map { createMap(it) }.sortedBy { it.sourceStart }
    val waterToLightMap = input.subList(89, 129).map { createMap(it) }.sortedBy { it.sourceStart }
    val lightToTemperatureMap = input.subList(131, 173).map { createMap(it) }.sortedBy { it.sourceStart }
    val temperatureToHumidityMap = input.subList(175, 210).map { createMap(it) }.sortedBy { it.sourceStart }
    val humidityToLocationMap = input.subList(212, 249).map { createMap(it) }.sortedBy { it.sourceStart }

    val location = seeds.map { seed ->
        val soilSource = goToSource(seed, seedToSoilMap)
        val fertilizeSource = goToSource(soilSource, soilToFertilizerMap)
        val waterSource = goToSource(fertilizeSource, fertilizerToWaterMap)
        val lightSource = goToSource(waterSource, waterToLightMap)
        val temperatureSource = goToSource(lightSource, lightToTemperatureMap)
        val humiditySource = goToSource(temperatureSource, temperatureToHumidityMap)
        goToSource(humiditySource, humidityToLocationMap)
    }.minOf { it }


    return location
}

fun solve1(input: List<String>): Long {
    val seeds = createSeeds(input[0])
    return getLowestLocation(seeds, input)
}


fun solve2(input: List<String>): Long {

    val seedRanges = createSeedRange(input[0])

    val seedToSoilMap = input.subList(3, 26).map { createMap(it) }.sortedBy { it.sourceStart }
    val soilToFertilizerMap = input.subList(28, 59).map { createMap(it) }.sortedBy { it.sourceStart }
    val fertilizerToWaterMap = input.subList(61, 87).map { createMap(it) }.sortedBy { it.sourceStart }
    val waterToLightMap = input.subList(89, 129).map { createMap(it) }.sortedBy { it.sourceStart }
    val lightToTemperatureMap = input.subList(131, 173).map { createMap(it) }.sortedBy { it.sourceStart }
    val temperatureToHumidityMap = input.subList(175, 210).map { createMap(it) }.sortedBy { it.sourceStart }
    val humidityToLocationMap = input.subList(212, 249).map { createMap(it) }.sortedBy { it.sourceStart }

    println(seedRanges)

    /*
    for (i in 0..18 step 2) {
        val seeds = createMoreSeeds(input[0], i)
        println("minRange: ${seeds.first()}, maxRange: ${seeds.last()}")

        println(getLowestLocation(seeds, input))
    }

     */

    // 0 -> 79753136
    // 2 -> 718857636
    // 4 ->
    // 6 ->
    // 8 -> 3802688341
    // 10 ->
    // 12 ->
    // 14 -> 1159584779
    // 16 ->
    // 18 ->


    return 0 // location
}

fun main() {

    val fr = FileReader("src/main/kotlin/day5/input.txt")
    val input = fr.readLines()


    println("Puzzle 1: " + solve1(input))

    val timeMillis = measureTimeMillis {
        println("Puzzle 2: " + solve2(input))
    }

    // 15 -17 ms
    println("Execution Time: $timeMillis ms")

}