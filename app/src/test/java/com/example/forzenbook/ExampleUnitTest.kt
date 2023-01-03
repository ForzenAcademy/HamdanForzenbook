package com.example.forzenbook

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
        2d array
        Given a 5x5 2d list, make a function that returns true if "FILES"
        can be made in the grid using Boggle rules
         */
        /*
        Find a starting point of F,
        check all tiles around that point for the next letter
         */
        val word = "FILES"
        val board = listOf(
            listOf("F", "I", "L", "E", "S"),
            listOf("I", "L", "E", "S", "F"),
            listOf("L", "E", "S", "F", "I"),
            listOf("E", "S", "F", "I", "L"),
            listOf("S", "F", "I", "L", "E")
        )
        val visited = mutableListOf<Pair<Int, Int>>()
        var x = false
        val dimen = board.size
        for (i in board.indices) {
            for (k in board.indices) {
                if (board[i][k] == word[0].toString()) {
                    visited.add(Pair(i, k))
                    x = explore(board, visited, i, k, word.substring(1))
                }
            }
        }
    }
}

fun explore(
    board: List<List<String>>,
    visited: MutableList<Pair<Int, Int>>,
    i: Int,
    k: Int,
    word: String
): Boolean {
    if (word.length == 1) return true

    if (i + 1 < board.size - 1 && k + 1 < board.size - 1 && !visited.contains(Pair(i + 1, k + 1))) {
        if (board[i + 1][k + 1] == word[0].toString()) {
            visited.add(Pair(i + 1, k + 1))
            explore(board, visited, i + 1, k + 1, word.substring(1))
        }
    }
    if (i + 1 < board.size - 1&& !visited.contains(Pair(i + 1, k))) {
        visited.add(Pair(i + 1, k + 1))
        explore(board, visited, i + 1, k + 1, word.substring(1))
    }
    if (k + 1 < board.size - 1&& !visited.contains(Pair(i, k + 1))) {
        visited.add(Pair(i + 1, k + 1))
        explore(board, visited, i + 1, k + 1, word.substring(1))
    }
    if (i - 1 > 0 && k - 1 > 0&& !visited.contains(Pair(i - 1, k - 1))) {
        visited.add(Pair(i + 1, k + 1))
        explore(board, visited, i + 1, k + 1, word.substring(1))
    }
    if (i - 1 > 0&& !visited.contains(Pair(i - 1, k))) {
        visited.add(Pair(i + 1, k + 1))
        explore(board, visited, i + 1, k + 1, word.substring(1))
    }
    if (k - 1 > 0&& !visited.contains(Pair(i, k - 1))) {
        visited.add(Pair(i + 1, k + 1))
        explore(board, visited, i + 1, k + 1, word.substring(1))
    }
    return false
}