package flac

import utils.Logger
import java.io.File
import java.time.LocalDateTime

class FileProcessor(
    private val metaFlacService: MetaFlacService,
    private val shellPropertyService: ShellPropertyService,
    private val musicDir: String,
    private val logger: Logger,
    private val removedCoversFile: String
) {
    fun processFlacFiles() {
        val dir = File(musicDir)
        if (!dir.exists()) {
            logger.log("Directory not found: $musicDir")
            return
        }

        logger.log("Starting processing of FLAC files in $musicDir")

        dir.walk()
            .filter { it.isFile && it.extension == "flac" }
            .toList()
            .parallelStream()
            .forEach { file -> processFile(file) }

        logger.log("Processing completed at ${LocalDateTime.now()}")
    }

    private fun processFile(file: File) {
        logger.log("\nProcessing ${file.absolutePath}\n")

        val properties = shellPropertyService.getPropertiesFromShell(file.absolutePath, logger)
        val title = properties["Title"].orEmpty()

        if (title.isBlank()) {
            handleMissingTitle(file)
        } else {
            logger.log("\nTitle found for: ${file.absolutePath}: $title\n")
        }
    }

    private fun handleMissingTitle(file: File) {
        logger.log("\nTitle NOT found for ${file.absolutePath} Removing padding...")
        metaFlacService.removePadding(file.absolutePath)

        val newTitle = shellPropertyService.getPropertiesFromShell(file.absolutePath, logger)["Title"].orEmpty()

        if (newTitle.isBlank()) {
            logger.log("\nTitle NOT yet found. Removing cover and padding...")
            metaFlacService.removePaddingAndPicture(file.absolutePath)
            File(removedCoversFile).appendText("${file.absolutePath}\n")

            val newTitle2 = shellPropertyService.getPropertiesFromShell(file.absolutePath, logger)["Title"].orEmpty()

            if (newTitle2.isBlank()) {
                logger.log("\nfile NOT recovered: ${file.absolutePath}")
            } else {
                logger.log("\nfile recovered: ${file.absolutePath}")
            }
        } else {
            logger.log("\nCorrected file: ${file.absolutePath}")
        }
    }
}
