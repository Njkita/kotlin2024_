import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

fun main() {
    val url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BE%D1%8E%D0%B7_%D0%A1%D0%BE%D0%B2%D0%B5%D1%82%D1%81%D0%BA%D0%B8%D1%85_%D0%A1%D0%BE%D1%86%D0%B8%D0%B0%D0%BB%D0%B8%D1%81%D1%82%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D1%85_%D0%A0%D0%B5%D1%81%D0%BF%D1%83%D0%B1%D0%BB%D0%B8%D0%BA"
    val content = fetchContent(url)
    val links = extractLinks(content)
    links.forEach { println(it) }
}

fun fetchContent(urlString: String): String {
    val url = URL(urlString)
    val connection = url.openConnection()
    val content = StringBuilder()

    BufferedReader(InputStreamReader(connection.getInputStream())).use { reader ->
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            content.append(line)
        }
    }

    return content.toString()
}

fun extractLinks(html: String): List<String> {
    val linkRegex = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"".toRegex()
    return linkRegex.findAll(html).map { matchResult ->
        val link = matchResult.groups[1]?.value ?: ""
        if (link.startsWith("/")) "https://en.wikipedia.org$link" else link
    }.toList()
}
