package com.hamdan.forzenbook

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        /*
            Write a function that takes in a list of ints and an int N.
            Return a list of Pairs of Ints from the list (no duplicates) that add together to equal N.
    } ?: Log.v("Hamdan", "no path")
         */

        val list = listOf(1, 3, 7, 8, 2, 4, 6, 9, 1, 2)
        val N = 4

        val pairs = list.distinct().mapNotNull { numOne ->
            if (numOne < N) {
                list.mapNotNull {
                    if (it + numOne == N) {
                        Pair(it, numOne)
                    } else null
                }.distinct()
            } else null
        }.distinct()

        val pog = list.mapNotNull { numOne ->
            if (list.find { numOne + it == N } != null) {
                Pair(numOne, list.find { numOne + it == N })
            } else null
        }.distinct()
        print("Over Here\n")
        print(pairs.toString())
        print("\nOver Here\n")

        print("Over Here\n")
        print(pog.toString())
        print("\nOver Here\n")
    }
}

fun viableCombos(numbers:List<Int>, resultant:Int): List<Pair<Int, Int?>> {
    return numbers.mapNotNull { numOne ->
        if (numbers.find { numOne + it == resultant } != null) {
            Pair(numOne, numbers.find { numOne + it == resultant })
        } else null
    }.distinct()
}

//fun traverse(
//    board: List<List<String>>,
//    x: Int,
//    y: Int,
//    route: MutableList<Pair<Int, Int>>
//): MutableList<Pair<Int, Int>>? {
//
//    if (board.getOrNull(x + 1)?.getOrNull(y) == "O") {
//        val n = mutableListOf<Pair<Int, Int>>()
//        route.forEach { n.add(it) }
//        n.add(Pair<x+1,y>)
//        traverse(board, x + 1, y, n)
//    }
//    if (board.getOrNull(x)?.getOrNull(y + 1) == "O") {
//        val n = mutableListOf<Pair<Int, Int>>()
//        route.forEach { n.add(it) }
//        traverse(board, x, y + 1, n)
//    }
//
//    return if (route.size == (board.size + board[0].size - 1)) {
//        route
//    } else {
//        null
//    }
//
//}