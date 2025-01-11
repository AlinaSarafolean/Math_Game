package com.example.mathgame

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var textScore: TextView
    lateinit var textLife: TextView
    lateinit var textTime: TextView
    lateinit var textQuestion: TextView
    lateinit var editTextAnswer: EditText
    lateinit var buttonOk: Button
    lateinit var buttonNext: Button

    var correctAnswer = 0
    var userScore = 0
    var userLife = 3

    lateinit var timer: CountDownTimer
    private val startTimerInMillis: Long = 20000
    var timeLeftInMillis: Long = startTimerInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        supportActionBar!!.title = "Addition"

        // Initialize views
        textScore = findViewById(R.id.textViewScore)
        textLife = findViewById(R.id.textViewLife)
        textTime = findViewById(R.id.textViewTime)
        textQuestion = findViewById(R.id.textViewQuestion)
        editTextAnswer = findViewById(R.id.editTextAnswer)
        buttonOk = findViewById(R.id.buttonOk)
        buttonNext = findViewById(R.id.buttonNext)

        // Start the game
        gameContinue()

        buttonOk.setOnClickListener {
            val input = editTextAnswer.text.toString()

            if (input == "") {
                Toast.makeText(applicationContext, "Please write an answer or click the next button", Toast.LENGTH_LONG).show()
            } else {
                try {
                    pauseTimer()
                    val userAnswer = input.toInt()

                    if (userAnswer == correctAnswer) {
                        userScore += 10
                        textQuestion.text = "Congratulations, your answer is correct"
                        textScore.text = userScore.toString()
                    } else {
                        userLife--
                        textQuestion.text = "Sorry, your answer is wrong"
                        textLife.text = userLife.toString()
                    }
                } catch (e: NumberFormatException) {
                    // Handle case where the user enters non-numeric input
                    Toast.makeText(applicationContext, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonNext.setOnClickListener {
            pauseTimer()
            resetTimer()
            gameContinue()  // Generate a new question
            editTextAnswer.setText("")  // Clear the answer field
        }
    }

    // Function to continue the game by generating a new question
    private fun gameContinue() {
        val number1 = Random.nextInt(0, 100)
        val number2 = Random.nextInt(0, 100)

        textQuestion.text = "$number1 + $number2"
        correctAnswer = number1 + number2

        startTimer()  // Start the timer for the new question
    }

    // Start the timer for the countdown
    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateText()  // Update the time display on each tick
            }

            override fun onFinish() {
                pauseTimer()  // Stop the timer when it finishes
                resetTimer()  // Reset the timer to its initial value
                updateText()  // Update the time display

                userLife--  // Decrease life if time is up
                textLife.text = userLife.toString()
                textQuestion.text = "Sorry, Time is up!"  // Show time-up message
            }
        }.start()
    }

    // Update the time text view
    private fun updateText() {
        val remainingTime: Int = (timeLeftInMillis / 1000).toInt()
        textTime.text = String.format(Locale.getDefault(), "%02d", remainingTime)  // Format time
    }

    // Pause the timer
    private fun pauseTimer() {
        timer.cancel()
    }

    // Reset the timer to the starting time
    private fun resetTimer() {
        timeLeftInMillis = startTimerInMillis
        updateText()  // Update the time display
    }
}
