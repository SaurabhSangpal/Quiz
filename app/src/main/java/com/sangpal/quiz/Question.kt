package com.sangpal.quiz

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: Int
) {
}