package ph.theorangeco.data.models.states

open class UiState {
    data object Initial : UiState()
    data object Loading : UiState()
    data object NoData : UiState()
    data class Success(val any: Any? = null) : UiState()
    data class Invalid(var msg: String): UiState()
    data class Failed(var msg: String?): UiState(){
        var msgString = "Something went wrong"
        init {
            msg?.let { msgString = it }
        }
    }
}


fun UiState.isInitial() = this is UiState.Initial
fun UiState.isProcessing() = this is UiState.Loading
fun UiState.isSuccess() = this is UiState.Success
fun UiState.isFailed() = this is UiState.Failed

@Suppress("UNCHECKED_CAST")
fun <T>UiState.getData() = (this as UiState.Success).any as T

