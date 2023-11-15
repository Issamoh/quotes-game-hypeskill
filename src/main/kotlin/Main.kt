import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import okhttp3.OkHttpClient
import  okhttp3.Request
import okhttp3.Response

private val client =  OkHttpClient()




fun main() {
    print("Enter quote ID: ")
    val quoteID = readLine()!!.toInt()
    val request = Request.Builder()
        .url("https://quotes-hyperskill.vercel.app/api/quotes/$quoteID")
        .build()

    client.newCall(request).execute().use { response ->
        val jsonString = response.body!!.string()
        println(jsonString)
    }
}