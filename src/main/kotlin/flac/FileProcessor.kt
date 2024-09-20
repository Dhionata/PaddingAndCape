package flac

import utils.Logger
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

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

        val totalTimeForFile = AtomicLong(0)    // Acumulador para o tempo total
        val processedCount = AtomicInteger(0)  // Contador de arquivos processados

        val totalTime = measureTime {
            dir.walk().filter { it.isFile && it.extension == "flac" }
                .toList()
                .parallelStream()
                .forEach { file ->
                    val duration = measureTimeMillis {
                        processFile(file)
                    }

                    totalTimeForFile.addAndGet(duration)      // Adiciona o tempo de processamento ao total
                    val count = processedCount.incrementAndGet()  // Incrementa o contador de arquivos processados

                    val averageTime = totalTimeForFile.get() / count
                    logger.log(
                        "Arquivo: ${file.name} | Tempo: ${duration}ms | Média atual: ${averageTime}ms | Contagem de arquivos: $count"
                    )
                }
        }

        logger.log("Processing completed at ${LocalDateTime.now()} in $totalTime")
    }

    private fun processFile(file: File) {
        logger.log("\nProcessing ${file.absolutePath}\n")

        val properties = shellPropertyService.getPropertiesFromShell(file.absolutePath, logger)
        val title = properties["Title"].orEmpty()

        if (title.isBlank()) {
            handleMissingTitle(file)
        } else {
            logger.log("\nTitle found for: ${file.name}: $title\n")
        }
    }

    private fun handleMissingTitle(file: File) {
        logger.log("\nTitle NOT found for ${file.name} Removing padding...")
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
