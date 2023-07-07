package com.victoryvalery.numbers.presentation.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victoryvalery.numbers.data.repository.GameRepositoryImpl
import com.victoryvalery.numbers.domain.entities.GameSettings
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.domain.entities.Question
import com.victoryvalery.numbers.domain.usecases.GenerateQuestionUseCase
import com.victoryvalery.numbers.domain.usecases.GetGameSettingsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _formattedTime = MutableStateFlow("")
    val formattedTime = _formattedTime.asStateFlow()

    private val _gameState = MutableStateFlow(GameState(gameSettings = GameSettings(0, 0, 0, 0)))
    val gameState = _gameState.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    private lateinit var gameSettings: GameSettings

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
    }

    fun sendAnswer(answer: Int) {
        val correct = if (gameState.value.question.sum - gameState.value.question.visibleNumber == answer) 1 else 0
        val newQuestion = generateQuestionUseCase(gameSettings.maxSumValue)
        val currentProgress =
            100 * (_gameState.value.countOfRightAnswers + correct).div(_gameState.value.countOfQuestions.toDouble() + 1)
        _gameState.value = gameState.value.copy(
            gameSettings = gameSettings,
            countOfRightAnswers = _gameState.value.countOfRightAnswers + correct,
            countOfQuestions = _gameState.value.countOfQuestions + 1,
            question = newQuestion,
            progress = currentProgress,
            greenProgress = currentProgress >= gameSettings.minPercentOfRightAnswers
        )
    }

    private fun generateQuestion() {
        val newQuestion = generateQuestionUseCase(gameSettings.maxSumValue)
        _gameState.value = gameState.value.copy(
            question = newQuestion,
            minCountOfRightAnswers = gameSettings.minCountOfRightAnswers
        )
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_TO_SECOND,
            MILLIS_TO_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        countDownTimer?.start()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_TO_SECOND
        val minutes = seconds / SECONDS_TO_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_TO_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }

    private fun finishGame() {
        _gameState.value = _gameState.value.copy(
            isFinished = true,
            winner = gameState.value.countOfRightAnswers >= gameSettings.minCountOfRightAnswers
                    && gameState.value.progress >= gameSettings.minPercentOfRightAnswers
        )
    }

    companion object {
        private const val MILLIS_TO_SECOND = 1000L
        private const val SECONDS_TO_MINUTES = 60
    }

}