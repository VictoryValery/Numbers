package com.victoryvalery.numbers.presentation.game

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.victoryvalery.numbers.R
import com.victoryvalery.numbers.databinding.FragmentGameBinding
import com.victoryvalery.numbers.domain.entities.GameResult
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.presentation.GameFinishedFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    //получение аргументов из навигации ещё вариант
//    private val args by navArgs<GameFragmentArgs>()

    //создание вью модели с аргументами в конструкторе с помощью фабрики и обычное
//    private val viewModel: GameViewModel by viewModels()
    private val viewModel by lazy {

    //получение аргументов из навигации
    val args = GameFragmentArgs.fromBundle(requireArguments())
    val level = args.level

        ViewModelProvider(
            this,
            GameViewModelFactory(requireActivity().application, level)
        )[GameViewModel::class.java]
    }

//    private lateinit var level: Level

    private val tvOptions: MutableList<TextView> by lazy {
        mutableListOf<TextView>().apply {
            this.add(binding.tvOption1)
            this.add(binding.tvOption2)
            this.add(binding.tvOption3)
            this.add(binding.tvOption4)
            this.add(binding.tvOption5)
            this.add(binding.tvOption6)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        parseArgs()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvOptions.forEach { textView ->
            textView.setOnClickListener {
                viewModel.sendAnswer(textView.text.toString().toInt())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gameState.collectLatest {
                render(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.formattedTime.collectLatest {
                binding.tvTimer.text = it
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
        if (gameState.question.options.isNotEmpty())

            tvOptions.forEachIndexed { index, textView ->
                textView.text = gameState.question.options[index].toString()
            }

        binding.progressBar.setProgress(gameState.progress.toInt(), true)
        val gameColor = getColorByState(gameState.greenProgress)
        binding.progressBar.progressTintList = ColorStateList.valueOf(gameColor)
        binding.tvAnswersProgress.setTextColor(gameColor)
        binding.progressBar.secondaryProgress = gameState.gameSettings.minPercentOfRightAnswers
        binding.tvAnswersProgress.text = getString(
            R.string.progress_answers,
            gameState.countOfRightAnswers.toString(),
            gameState.minCountOfRightAnswers.toString()
        )
    }

    private fun getColorByState(greenProgress: Boolean): Int {
        return if (greenProgress)
            ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        else
            ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        //1
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
//            .addToBackStack(null)
//            .commit()
        //2
//        val resBundle = Bundle().apply { putSerializable(GameFinishedFragment.GAME_RESULT, gameResult) }
//        findNavController().navigate(R.id.action_gameFragment_to_gameFinishedFragment, resBundle)

        //3
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )

    }

//    private fun parseArgs() {
//        level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requireArguments().getSerializable(LEVEL, Level::class.java) ?: Level.TEST
//        } else
//            requireArguments().getSerializable(LEVEL) as Level
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val LEVEL = "LEVEL"
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
