package com.example.unscramble

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import com.example.unscramble.ui.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class GameViewModel : ViewModel() {

    private lateinit var currentWord: String

    private val _uiState = MutableStateFlow(GameUiState())
    private var usedWords: MutableSet<String> = mutableSetOf()

    //asStateFlow()faz com que este estado mutável flua um fluxo de estado somente de leitura.
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random();
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle();
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord);
        }
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambleWord = pickRandomWordAndShuffle())
    }

    init {
        resetGame()
    }
}