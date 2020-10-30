package cz.legat.core.base

import cz.legat.core.HtmlParser
import retrofit2.Response

abstract class BaseRepository {

    val PARSER = HtmlParser()

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): NetworkResult<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return NetworkResult.Error(t)
        }

        if (!response.isSuccessful) {
            return NetworkResult.Error(Throwable("Unsuccessful call"))
        } else {
            if (response.body() == null) {
                return NetworkResult.Error(Throwable("Empty body"))
            }
        }
        return NetworkResult.Success(response.body()!!)
    }

}