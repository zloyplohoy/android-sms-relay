package ag.sokolov.smsrelay.domain.model

sealed class Response<out T, out E> {
    data class Success<out T>(val data: T? = null) : Response<T, Nothing>()
    data class Failure<out E>(val error: E) : Response<Nothing, E>()
    data object Loading : Response<Nothing, Nothing>()
}
