package flac

import java.io.File
import java.time.LocalDateTime
import java.util.logging.Logger

class FileProcessor(
    private val metaFlacService: MetaFlacService,
    private val shellPropertyService: ShellPropertyService,
    private val musicDir: String,
    private val removedCoversFile: String,
) {
    private val logger = Logger.getLogger(this.javaClass.name)

    fun processFlacFiles() {
        val dir = File(musicDir)
        if (!dir.exists()) {
            logger.warning("Directory not found: $musicDir")
            return
        }

        logger.info("Starting processing of FLAC files in $musicDir")

        dir.walk().filter { it.isFile && it.extension == "flac" }.toList().parallelStream().forEach { file ->
            processFile(file)
        }

        logger.info("Processing completed at ${LocalDateTime.now()}")
    }

    private fun processFile(file: File) {
        logger.info("\nProcessing ${file.absolutePath}\n")

        val properties = shellPropertyService.getPropertiesFromShell(file.absolutePath)
        val title = properties["Title"].orEmpty()

        if (title.isBlank()) {
            handleMissingTitle(file)
        } else {
            logger.info("\nTitle found for: ${file.name}: $title\n")
        }
    }

    private fun handleMissingTitle(file: File) {
        logger.warning("\nTitle NOT found for ${file.name} Removing padding...")
        metaFlacService.removePadding(file.absolutePath)

        val newTitle = shellPropertyService.getPropertiesFromShell(file.absolutePath)["Title"].orEmpty()

        if (newTitle.isBlank()) {
            logger.warning("\nTitle NOT yet found for ${file.name}. Removing cover and padding...")
            metaFlacService.removePaddingAndPicture(file.absolutePath)
            File(removedCoversFile).appendText("${file.absolutePath}\n")

            val newTitle2 = shellPropertyService.getPropertiesFromShell(file.absolutePath)["Title"].orEmpty()

            if (newTitle2.isBlank()) {
                logger.warning("\nfile NOT recovered: ${file.absolutePath}")
            } else {
                logger.info("\nfile recovered: ${file.absolutePath}")
            }
        } else {
            logger.info("\nCorrected file: ${file.absolutePath}")
        }
    }
}
