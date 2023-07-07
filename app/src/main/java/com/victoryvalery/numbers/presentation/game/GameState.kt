package com.victoryvalery.numbers.presentation.game

import com.victoryvalery.numbers.domain.entities.GameSettings
import com.victoryvalery.numbers.domain.entities.Question

data class GameState(
    val countOfRightAnswers: Int = 0,
    val countOfQuestions: Int = 0,
    val minCountOfRightAnswers: Int = 0,
    val question: Question = Question(0, 0, emptyList()),
    val progress: Double = 0.0,
    val isFinished: Boolean = false,
    val winner: Boolean = false,
    val greenProgress: Boolean = false,
    val gameSettings: GameSettings
)