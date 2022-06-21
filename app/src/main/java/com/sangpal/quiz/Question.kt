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
)