/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.quiz.solve.enter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import team.ommaya.wequiz.android.domain.usecase.quiz.GetAnonymousTokenUseCase
import javax.inject.Inject

@HiltViewModel
class QuizSolveAnonymousViewModel @Inject constructor(
    private val getAnonymousTokenUseCase: GetAnonymousTokenUseCase,
) : ViewModel() {

    private val _anonymousUiState: MutableSharedFlow<UiState> = MutableSharedFlow()
    val anonymousUiState = _anonymousUiState.asSharedFlow()

    fun getAnonymousToken(nickname: String) {
        viewModelScope.launch {
            _anonymousUiState.emit(UiState.Loading)
            getAnonymousTokenUseCase(nickname)
                .onSuccess {
                    _anonymousUiState.emit(UiState.Success(it))
                }.onFailure {
                    _anonymousUiState.emit(UiState.Fail(it.message ?: "네트워크 에러"))
                }
        }
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(
            val token: String,
        ) : UiState

        data class Fail(
            val message: String,
        ) : UiState
    }
}
