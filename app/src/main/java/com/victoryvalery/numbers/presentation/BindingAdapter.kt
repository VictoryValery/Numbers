package com.victoryvalery.numbers.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.victoryvalery.numbers.R

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = textView.context.getString(
        R.string.required_score,
        count.toString()
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = textView.context.getString(
        R.string.score_answers,
        count.toString()
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, count: Int) {
    textView.text = textView.context.getString(
        R.string.required_percentage,
        count.toString()
    )
}

@BindingAdapter("countOfRightAnswers", "countOfQuestions")
fun bindScorePercentage(textView: TextView, countOfRightAnswers: Int, countOfQuestions: Int) {
    textView.text = textView.context.getString(
        R.string.score_percentage,
        (100 * (countOfRightAnswers).div(countOfQuestions.toDouble())).toString()
    )
}

@BindingAdapter("resultEmoji")
fun bindResultEmoji(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(
        if (winner)
            R.drawable.ic_smile
        else
            R.drawable.ic_sad
    )
}