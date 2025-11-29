package com.example.metrogrid

import android.annotation.SuppressLint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> Flow<T>.singleOrError(
    success: ((T) -> Unit),
    failed: ((error: Throwable) -> Unit)? = null
) {
    try {
        success.invoke(single()!!)
    } catch (t: CancellationException) {
        LogHandler.e(t.toString())
    } catch (t: UnknownHostException) {
        val throwable = UnknownHostException("Please check your internet connection.")
        failed?.invoke(throwable)
    }
    catch (t: Throwable) {
        LogHandler.e(t.toString())
        failed?.invoke(t)
    }
}

sealed class Response<T>(
    val data: T? = null,
    val message: String? = null
) {

    class  Success<T>(data:T) : Response<T>(data)

    class Error<T>(message: String, data: T? = null): Response<T>(data, message)

    //   class Loading<T>(data: T? = null) : Response<T>(data)
}



fun <T> retrofit2.Response<T>.process():T  {

    if (isSuccessful) {
        LogHandler.d("isSuccess")
        return body()!! ?: throw ApiError("Data model parsing issue")
    } else {
        LogHandler.d("Error else ")

        try {
            if (code()==503){
                throw ApiError("Service Unavailable")
            }

            throw ApiError(errorBody()!!.string())
        } catch (ex: Exception){
            LogHandler.d("Error exc")

            throw ApiError(ex.message.toString())
        } catch (ex: HttpException){
            LogHandler.d("Error httpexc")

            throw ApiError(ex.message?:"empty httpException" ,ex.code())
        }
    }
}
object LogHandler {
    private const val tag = "METROGRID_APP"

    fun e(ex: Exception){
        Timber.tag(tag).e(ex)
    }

    fun e(msg: String){
        Timber.tag(tag).e(msg)
    }

    fun d(msg: String){
        Timber.tag(tag).d(msg)
    }

    fun w(ex: Exception){
        Timber.tag(tag).w(ex)
    }

    fun w(msg: String){
        Timber.tag(tag).w(msg)
    }
}

class ApiError(private val msg: String = "Api Error", private val statusCode: Int = 0): Throwable(msg){
    fun errorMessage() = msg
    fun statusCode() = statusCode
}



@SuppressLint("SimpleDateFormat")
fun yyyyMMddTHHmmssToRegularDate(dateStr: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssss", Locale.ENGLISH)
    val convertedDate: Date? = formatter.parse(dateStr)
    return SimpleDateFormat("MM-dd-yyyy").format(convertedDate).toString()
}
fun yHHmmssToRegularDate(dateStr: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH)
    val convertedDate: Date? = formatter.parse(dateStr)
    return SimpleDateFormat("HH:mm").format(convertedDate).toString()
}

fun epochtToMMddyyyyhhmmaaa(epoch: Long): String {
    val sdf = SimpleDateFormat("MM dd, yyyy\nhh:mm aaa")
    val netDate = Date(epoch * 1000)
    return sdf.format(netDate)
}

fun epochtToEEEddMMyyyy(epoch: Long): String {
    val sdf = SimpleDateFormat("EEE, dd/MM/yyyy")
    val netDate = Date(epoch * 1000)
    return sdf.format(netDate)
}
fun epochtTohhmmaaa(epoch: Long): String {
    val sdf = SimpleDateFormat("hh:mm aaa")
    val netDate = Date(epoch * 1000)
    return sdf.format(netDate)
}
fun offsetDateTime(oDT: String?):String?{
    if (oDT.isNullOrEmpty()) return null
    val value = OffsetDateTime.parse(oDT)

    return value.format(DateTimeFormatter.ofPattern("hh:mm a"))
}
fun secToMinutes(sec: Int):String{
    return "${sec/60}"
}

val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
