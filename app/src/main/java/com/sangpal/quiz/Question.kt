package com.sangpal.quiz

import kotlinx.serialization.Serializable

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
    }

    fun getQuestionByLvl(level: Int): Question {
        when (level) {
            1 -> return l1[0]
            2 -> return l2[0]
            3 -> return l3[0]
            4 -> return l4[0]
            5 -> return l5[0]
            6 -> return l6[0]
            7 -> return l7[0]
            8 -> return l8[0]
            9 -> return l9[0]
            10 -> return l10[0]
            11 -> return l11[0]
            12 -> return l12[0]
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
}