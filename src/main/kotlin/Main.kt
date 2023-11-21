import okhttp3.OkHttpClient
import  okhttp3.Request
import okhttp3.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import kotlin.random.Random

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

    val url2 = "https://quotes-hyperskill.vercel.app/api/authors/${quoteData.author_id}"
    val authorJson = getDataFrom(url2)
    val authorData = Json.decodeFromString<Author>(authorJson)

    //We get the maximum number of authors included in the database
    val url3 = "https://quotes-hyperskill.vercel.app/api/authors/total"
    val totalJson = getDataFrom(url3)
    val totalAuthors = totalJson.toInt()

    //We get three valid different authors IDs
    val randomIDs = mutableListOf<Int>()
    do {
        val randomID = Random.nextInt(1, totalAuthors+1)
        if (randomID != authorData.id && !randomIDs.contains(randomID)) randomIDs.add(randomID)
    }  while (randomIDs.size < 3)

    //We decalre a collection of authors' names which includes the correct author name
    val authorsNames = mutableListOf<String>(authorData.name)

    //We retrieve 3 authors names and add them to the collection
    for (id in randomIDs) {
        val url = "https://quotes-hyperskill.vercel.app/api/authors/$id"
        val randomAuthorJson = getDataFrom(url)
        val randomAuthorData = Json.decodeFromString<Author>(randomAuthorJson)
       authorsNames.add(randomAuthorData.name)
    }

    println(quoteData.text)
    var index = 1
    for (name in authorsNames.shuffled()) {
        println("$index. $name")
        index++
    }

    print("Guess the author by entering its associated number: ")
    val guess = readln().toInt()
    if (authorsNames[guess-1] == authorData.name) println("Correct! Well done!")
    else println("Almost there! Give it another shot!")


}