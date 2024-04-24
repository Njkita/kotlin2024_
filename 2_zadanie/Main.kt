import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val url = "https://ru.wikipedia.org/wiki/%D0%9F%D1%81%D0%B5%D0%B2%D0%B4%D0%BE%D1%8D%D1%84%D0%B5%D0%B4%D1%80%D0%B8%D0%BD"
    val Gettinglink = Geturl(url)
    val links = derivelinks(Gettinglink)
    for (link in links) {
        println(link)
    }
}

fun Geturl (Stringurl: String): String {
    val url = URL(Stringurl)
    val compound = url.openConnection()
    val allline = StringBuilder()

    BufferedReader(InputStreamReader(compound.getInputStream())).use { reader ->
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            allline.append(line)
        }
    }

    return allline.toString()
}

fun derivelinks(html: String): List<String> {
    val linkRegular = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"".toRegex()
    return linkRegular.findAll(html).map { output ->
        val link = output.groups[1]?.value ?: ""
        if (link.startsWith("/")) "https://en.wikipedia.org" + link else link
    }.toList()
}
