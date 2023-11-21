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
    var score = 0
    var round = 1
    var playAgain = true
    while (playAgain) {
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

        val authorsNamesShuffled = authorsNames.shuffled()
        println(quoteData.text)
        var index = 1
        for (name in authorsNamesShuffled) {
            println("$index. $name")
            index++
        }


        var attempts = 0
        while (attempts < 3) {
            print("Guess the author by entering its associated number: ")
            val guess = readln().toInt()
            if (authorsNamesShuffled[guess - 1] == authorData.name) {
                println("Correct! Well done!")
                println("The correct author is ${authorData.name}. ${authorData.name.split(" ")[0]} is ${authorData.bio}")
                score++
                break
            } else {
                giveHint(attempts, authorData)
            }
            attempts++
        }
        println("Congratulations! You've completed $round rounds. Your total score is $score. Keep learning and keep up the fantastic work!")
        round++
        print("Do you want to play again? (yes/no): ")
        val userInput = readln().trim()
        when (userInput) {
            "yes" -> {
                // Continue playing
            }
            "no" -> {
                println("Thanks for playing! Goodbye!")
                playAgain = false
            }
//            else -> {
//                println("Invalid input. Please enter 'yes' or 'no'.")
//            }
        }
    }
}

fun giveHint(attempts: Int, author: Author){
    when(attempts) {
        0 -> {
            println("Almost there! Give it another shot!")
            println("Here's a hint, the author is born on ${author.birthdate}")
        }
        1 -> {
            println("Almost there! Give it another shot!")
            println("Here's another hint, the author is ${author.hint}")
        }
        2 -> {
            println("You've run out of hints!")
            println("The correct author is ${author.name}. ${author.name.split(" ")[0]} is ${author.bio}")
            println("Nice effort! Don't worry. You'll get the next one!")
        }
    }
}