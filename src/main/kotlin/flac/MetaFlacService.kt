package flac

import utils.Sanitizer
import windows.PowerShell
import java.io.IOException
import java.util.logging.Logger

class MetaFlacService(private val metaFlacPath: String, private val powerShell: PowerShell = PowerShell()) {

    private val logger: Logger = Logger.getLogger(this.javaClass.name)

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
            val metaFlacCommand = "$metaFlacPath $command"
            logger.info("metaFlacCommand: $metaFlacCommand")

            val powershellReturn = powerShell.executeCommand(metaFlacCommand)
            logger.info { powershellReturn }
        } catch (e: IOException) {
            logger.warning("Error when executing the metaFlac command: ${e.message}")
            throw Exception(e)
        }
    }
}
