package cz.legat.prectito.ui.main.base

import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return Result.Error(t)
        }

        if (!response.isSuccessful) {
            return Result.Error(Throwable("Unsuccessful call"))
        } else {
            if (response.body() == null) {
                return Result.Error(Throwable("Empty body"))
            }
        }
        return Result.Success(response.body()!!)
    }

}