package utils

object Sanitizer {

    fun sanitizeFilePath(filePath: String): String {
        var filePath = filePath
        return if (filePath.contains("“") || filePath.contains("”") || filePath.contains("$")) {
            filePath = filePath.replace("“", "`“")
            filePath = filePath.replace("”", "`”")
            filePath = filePath.replace("$", "`$")
            filePath
        } else {
            filePath
        }
    }
}
