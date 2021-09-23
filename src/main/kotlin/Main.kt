import java.io.BufferedWriter
import java.io.File
import kotlin.math.ceil
import kotlin.math.log10

// number of values to random
const val n = 10000
// initial generator values
const val k0: Long = 0
const val k1: Long = 470001
const val q: Long = 999563
const val w0: Long = 5151 + 11
// number of intervals to divide the selection
var numOfIntervals: Int = (3.31 * log10(n.toDouble()) + 1.0).toInt()
//var numOfIntervals: Int = 13
const val histogramX = "histogramX.txt"
const val histogramY = "histogramY.txt"

fun main(args: Array<String>) {
    println("Hello World!")
    // list of unsorted uniform random values
    val uniformValues = randUniformValues()
    // list of sorted uniform random values
    val sortedValues = uniformValues.sorted().toSet().toList()
    // min and max values to calculate interval length to build histogram
    val minValue = sortedValues[0]
    val maxValue = sortedValues[n - 1]
    // interval length
    val intervalLength = ceil((maxValue - minValue) / numOfIntervals.toDouble()).toLong()
    // list with uniform random values frequency on each interval
    val frequencyList = getAbsoluteFrequency(numOfIntervals, sortedValues)
    // list with interval's boundaries to build histogram
    val histogramBoundaries = getHistogramBoundaries(intervalLength, numOfIntervals, minValue, maxValue)
    // list with uniform random values frequency scaled to build both histogram and theoretical function on the
    // same chart
    val scaledFrequencyValues = getScaledFrequencyValues(frequencyList, intervalLength)
//    writeFile(histogramBoundaries, frequencyList)
    writeFile(histogramBoundaries, scaledFrequencyValues)
    println(frequencyList)
    println(frequencyList.sum())
    // Try adding program arguments at Run/Debug configuration
    println("Program arguments: ${args.joinToString()}")
}

/**
 *  The function calculates scaled random values frequency on each interval to
 *  build both theoretical and empirical function (histogram) on the same chart with the same scale
 */

fun getScaledFrequencyValues(frequencyList: List<Int>, intervalLength: Long): List<Double> {
    val scaledValues = mutableListOf<Double>()
    for (element in frequencyList) {
        // divide each unscaled value by total values amount (n) and histogram interval's length
        val tmp = (element.toDouble() / n)
        scaledValues.add( tmp / intervalLength)
    }
    return scaledValues
}

fun writeFile(histogramBoundaries: List<Any>, frequency: List<Any>) {
    val listSize = frequency.size
    File(histogramX).printWriter().use { out ->
        out.println(histogramBoundaries[0])
        out.println(histogramBoundaries[0])
        for (i in 1 until listSize-1) {
            for (j in 0..2) {
                out.println(histogramBoundaries[i])
//                out.println()
            }
        }
        out.println(histogramBoundaries[listSize-1])
        out.println(histogramBoundaries[listSize-1])
    }

    File(histogramY).printWriter().use { out ->
        out.println(0)
        for (i in 0 until listSize) {
            out.println(frequency[i])
            out.println(frequency[i])
            out.println(0)
        }
    }
}

private fun BufferedWriter.write(l: Long) {
    this.write(l)
    this.newLine()
}

/**
 *  The function calculates boundaries of each histogram's interval
 */

fun getHistogramBoundaries(
    intervalLength: Long,
    numOfIntervals: Int,
    minValue: Long,
    maxValue: Long
): List<Int> {
    println("interval length = $intervalLength")
    val boundariesList = mutableListOf<Int>()
    boundariesList.add(minValue.toInt())
    // start at 1 as we add min value as the beginning of the first interval
    // formula for the next interval: i * intervalLength + minValue (minValues needed because we don't reach the
    // end of the interval without adding it)
    for (i in 1 until numOfIntervals) {
        boundariesList.add((i * intervalLength.toInt() + minValue).toInt())
    }
    boundariesList.add(maxValue.toInt())
//    boundariesList.add()
    println(boundariesList)
    return boundariesList
}

/**
 * The function to random uniform distribution values
 */

fun randUniformValues() : List<Long> {
    val numbersList: MutableList<Long> = mutableListOf()
    numbersList.add(w0.toLong())
    for (i in 1 until n) {
        val num: Long = (k1 * numbersList[i - 1] + k0).mod(q)
        numbersList.add(num)
    }
    return numbersList
}

/**
 * The function to get the absolute frequency (amount of numbers) on each interval.
 */

fun getAbsoluteFrequency(
    numOfIntervals: Int,
    uniformValuesList: List<Long>
): List<Int> {
    val minValue = uniformValuesList[0]
    val maxValue = uniformValuesList[n - 1]
    val intervalLength = ceil((maxValue - minValue) / numOfIntervals.toDouble()).toLong()
//    println("interval length = $intervalLength")
    val frequencyList: MutableList<Int> = mutableListOf()
    var j = 0
    var sum = 0
    for (i in 0 until numOfIntervals) {
        while (j < n && uniformValuesList[j] < intervalLength * (i + 1) + minValue) {
            sum++
            j++
        }
        println("interval = $i, sum = $sum")
//        println("uniformVal[${j-1}]=${uniformValuesList[j-1]}, uniformVal[${j}]=${uniformValuesList[j]}, ")
        if (j+1 < n) { println("uniformValuesList[${j+1}]=${uniformValuesList[j+1]} ")}
        println(uniformValuesList[n-1])
        frequencyList.add(sum)
        sum = 0
//    val incompleteSum = frequencyList.sum()
//    frequencyList.add(n - incompleteSum)}
    }
    return frequencyList
}

