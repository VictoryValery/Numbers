package com.victoryvalery.numbers.domain.usecases

import com.victoryvalery.numbers.domain.entities.GameSettings
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.domain.repository.GameRepository

class GetGameSettingsUseCase(
    val gameRepository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return gameRepository.getGameSettings(level)
    }

}