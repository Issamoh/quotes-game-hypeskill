import okhttp3.OkHttpClient
import  okhttp3.Request
import okhttp3.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

private val client =  OkHttpClient()

fun getDataFrom(inputUrl: String) : String{
    val request = Request.Builder()
        .url(inputUrl)
        .build()

    client.newCall(request).execute().use { response ->
        return response.body!!.string()
    }
}

@Serializable
data class Quote(val id: Int, val text: String, val author_id: Int, val tags: List<String>)

@Serializable
data class Author(val id: Int, val name: String, val birthdate: String, val hint: String, val bio: String)

fun main() {
    val url = "https://quotes-hyperskill.vercel.app/api/quotes/random"
    val quoteJson = getDataFrom(url)
    val quoteData = Json.decodeFromString<Quote>(quoteJson)
    println(quoteData.text)

    val url2 = "https://quotes-hyperskill.vercel.app/api/authors/${quoteData.author_id}"
    val authorJson = getDataFrom(url2)
    val authorData = Json.decodeFromString<Author>(authorJson)
    println(authorData.name)
}