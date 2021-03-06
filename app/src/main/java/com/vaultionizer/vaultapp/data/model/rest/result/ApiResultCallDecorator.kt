package com.vaultionizer.vaultapp.data.model.rest.result

import android.util.Log
import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResultCallDecorator<T : Any>(val proxy: Call<T>, val typeClass: Class<*>) :
    Call<ApiResult<T>> {

    override fun clone(): Call<ApiResult<T>> = ApiResultCallDecorator(proxy.clone(), typeClass)

    override fun execute(): Response<ApiResult<T>> = TODO()

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        Log.e("Vault", "ENQUEUE")
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() in (200 until 300)) {
                    /** If a service wants to ignore the result of a rest call the generic type T should be Unit
                     * For now, we just assume the service specified unit as type if the content length is 0 (no response body)
                     * TODO(jatsqi): Implement better generic type handling.
                     * TODO(jatsqi): Option: Use the unwrapped class type from the factory
                     */
                    if (typeClass.equals(Unit::class.java)) {
                        callback.onResponse(
                            this@ApiResultCallDecorator,
                            Response.success(ApiResult.Success(Unit) as ApiResult<T>)
                        )
                    } else {
                        callback.onResponse(
                            this@ApiResultCallDecorator,
                            Response.success(ApiResult.Success(response.body()!!))
                        )
                    }
                } else {
                    callback.onResponse(
                        this@ApiResultCallDecorator,
                        Response.success(ApiResult.Error(statusCode = response.code()))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is IOException) {
                    callback.onResponse(
                        this@ApiResultCallDecorator,
                        Response.success(ApiResult.NetworkError(t))
                    )
                } else {
                    TODO("Improve error handling ${t.localizedMessage}")
                    // callback.onResponse(this@ApiResultCallDecorator, Response.success(ApiResult.Error(statusCode = -1)))
                }
            }
        })
    }

    override fun isExecuted() = proxy.isExecuted

    override fun cancel() = proxy.cancel()

    override fun isCanceled(): Boolean = proxy.isCanceled

    override fun request(): Request = proxy.request()

    override fun timeout(): Timeout = proxy.timeout()

}