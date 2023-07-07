package com.victoryvalery.numbers.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victoryvalery.numbers.data.repository.GameRepositoryImpl
import com.victoryvalery.numbers.domain.entities.GameSettings
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.domain.usecases.GenerateQuestionUseCase
import com.victoryvalery.numbers.domain.usecases.GetGameSettingsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
) : ViewModel() {

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

}