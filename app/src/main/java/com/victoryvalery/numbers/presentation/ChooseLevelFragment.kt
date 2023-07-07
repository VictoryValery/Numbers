package com.victoryvalery.numbers.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.victoryvalery.numbers.R
import com.victoryvalery.numbers.databinding.FragmentChooseLevelBinding
import com.victoryvalery.numbers.domain.entities.Level
import com.victoryvalery.numbers.presentation.game.GameFragment
import java.lang.RuntimeException

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLevelTest.setOnClickListener {
            launchGameFragment(Level.TEST)
        }
        binding.buttonLevelEasy.setOnClickListener {
            launchGameFragment(Level.EASY)
        }
        binding.buttonLevelNormal.setOnClickListener {
            launchGameFragment(Level.MEDIUM)
        }
        binding.buttonLevelHard.setOnClickListener {
            launchGameFragment(Level.HARD)
        }

    }

    private fun launchGameFragment(level: Level) {
        //1
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, GameFragment.newInstance(level))
//            .addToBackStack(GameFragment.NAME)
//            .commit()

        //2
//        val gameBundle = Bundle().apply {putSerializable(GameFragment.LEVEL, level) }
//        findNavController().navigate(R.id.action_chooseLevelFragment_to_gameFragment, gameBundle)

        //3
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val NAME = "ChooseLevelFragment"

        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }

}
