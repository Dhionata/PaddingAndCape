package flac

import utils.Logger
import utils.Sanitizer
import java.io.IOException

class MetaFlacService(private val metaFlacPath: String, private val logger: Logger) {
    private val powerShell = PowerShell()

    fun removePadding(fileAbsolutePath: String) {
        val fileAbsolutePath = Sanitizer.sanitizeFilePath(fileAbsolutePath)
        executeCommand("--remove --block-type=PADDING --dont-use-padding \"$fileAbsolutePath\"")
    }

    fun removePaddingAndPicture(fileAbsolutePath: String) {
        val fileAbsolutePath = Sanitizer.sanitizeFilePath(fileAbsolutePath)
        executeCommand("--remove --block-type=PICTURE --dont-use-padding \"$fileAbsolutePath\"")
        removePadding(fileAbsolutePath)
    }

    private fun executeCommand(command: String) {
        try {
            val metaFlacCommand = "\"$metaFlacPath\" $command"
            logger.log("metaFlacCommand: $metaFlacCommand")

            powerShell.executeCommand(metaFlacCommand, logger)
        } catch (e: IOException) {
            logger.log("Error when executing the metaFlac command: ${e.message}")
            throw Exception(e)
        }
    }
}
