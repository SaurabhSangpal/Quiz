package com.sangpal.quiz

import kotlinx.serialization.Serializable

// A is 1, B is 2, C is 3, D is 4, correctAnswer points to the index of the option.
@Serializable
class Question(
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: Int
)

@Serializable
class Root(val question: Question)

@Serializable
class AllQuestions(
    val l1: Array<Question>,
    val l2: Array<Question>,
    val l3: Array<Question>,
    val l4: Array<Question>,
    val l5: Array<Question>,
    val l6: Array<Question>,
    val l7: Array<Question>,
    val l8: Array<Question>,
    val l9: Array<Question>,
    val l10: Array<Question>,
    val l11: Array<Question>,
    val l12: Array<Question>
)