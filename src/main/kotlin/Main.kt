import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import utils.FileSelector
import utils.Logger
import java.io.File
import java.util.Date
import javax.swing.JOptionPane

fun main() {
    try {
        val logger = Logger("flac_processing_log ${Date().hashCode()}.txt")

        val fileSelector = FileSelector()

        val metaFlacFile = fileSelector.selectMetaFlacPath()
        logger.log("Selected metaflac.exe file: ${metaFlacFile.absolutePath}")

        val musicDirectory = fileSelector.selectMusicDirectory()
        logger.log("Selected song directory: ${musicDirectory.absolutePath}")

        val removedCoversFile = File(".", "removed_covers ${Date().hashCode()}.txt").absolutePath

        val metaFlacService = MetaFlacService(metaFlacFile.absolutePath, logger)
        val shellPropertyService = ShellPropertyService()
        val fileProcessor = FileProcessor(
            metaFlacService, shellPropertyService, musicDirectory.absolutePath, logger,
            removedCoversFile
        )

        fileProcessor.processFlacFiles()

        JOptionPane.showMessageDialog(null, "Finished")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Finished with ERROR\n$e")
    }
}
