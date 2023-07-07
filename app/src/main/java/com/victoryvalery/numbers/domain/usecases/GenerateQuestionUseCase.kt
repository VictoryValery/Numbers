package com.victoryvalery.numbers.domain.usecases

import com.victoryvalery.numbers.domain.entities.Question
import com.victoryvalery.numbers.domain.repository.GameRepository

class GenerateQuestionUseCase(
    val gameRepository: GameRepository
) {

    operator fun invoke(maxSumValue: Int): Question {
        return gameRepository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS: Int = 6
    }

}