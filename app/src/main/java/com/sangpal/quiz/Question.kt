package com.sangpal.quiz

import kotlinx.serialization.Serializable
import kotlin.random.Random

// A is 1, B is 2, C is 3, D is 4, ans points to the index of the option.
@Serializable
class Question(
    val q: String,
    val a: String,
    val b: String,
    val c: String,
    val d: String,
    val ans: Int
)

@Serializable
class Root(val allQuestions: AllQuestions)

@Serializable
class AllQuestions(
    val l1: List<Question>,
    val l2: List<Question>,
    val l3: List<Question>,
    val l4: List<Question>,
    val l5: List<Question>,
    val l6: List<Question>,
    val l7: List<Question>,
    val l8: List<Question>,
    val l9: List<Question>,
    val l10: List<Question>,
    val l11: List<Question>,
    val l12: List<Question>
) {
    companion object {
        const val TOTAL_LEVELS = 12
        const val MAX_SCORE = 1000000
    }

    fun getQuestionByLvl(level: Int): Question {
        when (level) {
            1 -> return getRandomQuestion(l1)
            2 -> return getRandomQuestion(l2)
            3 -> return getRandomQuestion(l3)
            4 -> return getRandomQuestion(l4)
            5 -> return getRandomQuestion(l5)
            6 -> return getRandomQuestion(l6)
            7 -> return getRandomQuestion(l7)
            8 -> return getRandomQuestion(l8)
            9 -> return getRandomQuestion(l9)
            10 -> return getRandomQuestion(l10)
            11 -> return getRandomQuestion(l11)
            12 -> return getRandomQuestion(l12)
        }

        throw IndexOutOfBoundsException()
    }

    fun getScoreByLvl(level: Int): Int {
        when (level) {
            1 -> return 1000
            2 -> return 2000
            3 -> return 3000
            4 -> return 5000
            5 -> return 10000
            6 -> return 20000
            7 -> return 40000
            8 -> return 80000
            9 -> return 160000
            10 -> return 320000
            11 -> return 640000
            12 -> return 1000000
        }

        throw IndexOutOfBoundsException()
    }

    private fun getRandomQuestion(list: List<Question>): Question {
        val rand = Random.nextInt(0, list.size)
        return list[rand]
    }
}