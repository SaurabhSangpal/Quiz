package com.sangpal.quiz

import kotlinx.serialization.Serializable

// A is 1, B is 2, C is 3, D is 4, correctAnswer points to the index of the option.
@Serializable
data class Question(
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: Int
)

@Serializable
class AllQuestions(val questions: Array<Question>)