package com.victoryvalery.numbers.presentation.game

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.victoryvalery.numbers.R
import com.victoryvalery.numbers.databinding.FragmentGameBinding
import com.victoryvalery.numbers.domain.entities.GameResult
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.presentation.GameFinishedFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private val viewModel: GameViewModel by viewModels()

    private lateinit var level: Level

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        viewModel.startGame(level)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOption1.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption1.text.toString().toInt())
        }
        binding.tvOption2.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption2.text.toString().toInt())
        }
        binding.tvOption3.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption3.text.toString().toInt())
        }
        binding.tvOption4.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption4.text.toString().toInt())
        }
        binding.tvOption5.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption5.text.toString().toInt())
        }
        binding.tvOption6.setOnClickListener {
            viewModel.sendAnswer(binding.tvOption6.text.toString().toInt())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gameState.collectLatest {
                render(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timerState.collectLatest {
                val text = WeakReference("00:${if (it > 9) it else "0$it"}")
                binding.tvTimer.text = text.get()
            }
        }
    }

    private fun render(gameState: GameState) {

        if (gameState.isFinished) {
            val gameResult = GameResult(
                winner = gameState.winner,
                countOfRightAnswers = gameState.countOfRightAnswers,
                countOfQuestions = gameState.countOfQuestions,
                gameSettings = gameState.gameSettings
            )
            launchGameFinishedFragment(gameResult)
        }
        binding.tvSum.text = gameState.question.sum.toString()
        binding.tvLeftNumber.text = gameState.question.visibleNumber.toString()
        if (gameState.question.options.isNotEmpty()) {
            binding.tvOption1.text = gameState.question.options[0].toString()
            binding.tvOption2.text = gameState.question.options[1].toString()
            binding.tvOption3.text = gameState.question.options[2].toString()
            binding.tvOption4.text = gameState.question.options[3].toString()
            binding.tvOption5.text = gameState.question.options[4].toString()
            binding.tvOption6.text = gameState.question.options[5].toString()
        }

        binding.progressBar.progress = gameState.progress.toInt()
        binding.tvAnswersProgress.text = getString(
            R.string.progress_answers,
            gameState.countOfRightAnswers.toString(),
            gameState.minCountOfRightAnswers.toString()
        )
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(LEVEL, Level::class.java) ?: Level.TEST
        } else
            requireArguments().getSerializable(LEVEL) as Level
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val LEVEL = "LEVEL"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LEVEL, level)
                }
            }
        }
    }
}
