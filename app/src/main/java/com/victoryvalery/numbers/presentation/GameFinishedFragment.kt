package com.victoryvalery.numbers.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.victoryvalery.numbers.R
import com.victoryvalery.numbers.databinding.FragmentGameFinishedBinding
import com.victoryvalery.numbers.domain.entities.GameResult
import com.victoryvalery.numbers.presentation.game.GameFragment
import java.lang.RuntimeException


class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

//    private val _res by lazy { parseArg() }
//    private val res get() = _res ?: throw RuntimeException("There is no game result")

    private val _res by navArgs<GameFinishedFragmentArgs>()
    private val res get() = _res.gameResult


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //указали в nav_graph то же самое в PopUpTo и с Inclusive = true (вернуться к такому-то фрагменту
        // и удалить в том числе и его, чтобы не зависеть от изменений в цепочке перед этим фрагментом
        // ивсегда возвращаться к фрагменту СРАЗУ ДО фрагмента игры. Вдруг мы ДО игры новый фрагмент добавим?
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    onRetryGame()
//                }
//            })


        //УСТАНОВКА ДЛЯ DATABINDING
        binding.gameResult = res

        binding.buttonRetry.setOnClickListener {
            onRetryGame()
        }

        binding.emojiResult.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                if (res.winner)
                    R.drawable.ic_smile
                else
                    R.drawable.ic_sad
            )
        )
    }

    private fun onRetryGame() {
        //1
//        requireActivity().supportFragmentManager
//            .popBackStack(GameFragment.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        //2
        findNavController().popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val GAME_RESULT = "GAME_RESULT"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(GAME_RESULT, gameResult)
                }
            }
        }
    }

}
