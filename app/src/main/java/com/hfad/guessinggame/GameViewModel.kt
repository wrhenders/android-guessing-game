package com.hfad.guessinggame


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val words = listOf("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase()
    val secretWordDisplay = MutableLiveData<String>()
    var correctGuesses = ""
    val incorrectGuesses = MutableLiveData<String>("")
    val livesLeft = MutableLiveData<Int>(8)
    val gameOver = MutableLiveData<Boolean>(false)

    init {
        secretWordDisplay.value = deriveSecretWordDisplay()
    }

    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    private fun checkLetter(str:String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> '_'
    }

    fun makeGuess (guess: String) {
        if(guess.length == 1){
            if(secretWord.contains(guess)) {
                correctGuesses += guess
                secretWordDisplay.value = deriveSecretWordDisplay()
            } else {
                incorrectGuesses.value += "$guess "
                livesLeft.value = livesLeft.value?.minus(1)
            }
            if (isWon() || isLost()) gameOver.value = true
        }
    }

    private fun isWon() = secretWord.equals(secretWordDisplay.value, true)
    private fun isLost() = livesLeft.value ?: 0 <=0

    fun wonLostMessage(): String {
        var message = ""
        if(isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"
        message += " The word was $secretWord."
        return message
    }

    fun finishGame() {
        gameOver.value = true
    }
}