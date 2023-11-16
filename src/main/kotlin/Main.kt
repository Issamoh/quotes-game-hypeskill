import okhttp3.OkHttpClient
import  okhttp3.Request
import okhttp3.Response

private val client =  OkHttpClient()

fun getDataFrom(inputUrl: String) : String{
    val request = Request.Builder()
        .url(inputUrl)
        .build()

    client.newCall(request).execute().use { response ->
        return response.body!!.string()
    }
}


fun main() {
    print("Enter quote ID: ")
    val quoteID = readLine()!!.toInt()
    val url = "https://quotes-hyperskill.vercel.app/api/quotes/$quoteID"
    val response = getDataFrom(url)
    print(response)
}