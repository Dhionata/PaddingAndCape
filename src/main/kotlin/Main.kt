import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import utils.FileSelector
import java.io.File
import java.util.*
import java.util.logging.Logger
import javax.swing.JOptionPane

fun main() {
    try {
        val logger = Logger.getGlobal()
        val fileSelector = FileSelector()

        val metaFlacFile = fileSelector.selectMetaFlacPath()
        logger.info("Selected metaflac.exe file: ${metaFlacFile.absolutePath}")

        val musicDirectory = fileSelector.selectMusicDirectory()
        logger.info("Selected song directory: ${musicDirectory.absolutePath}")

        val removedCoversFile = File(".", "removed_covers ${Date().hashCode()}.txt").absolutePath

        val metaFlacService = MetaFlacService(metaFlacFile.absolutePath)
        val shellPropertyService = ShellPropertyService()

        FileProcessor(
            metaFlacService, shellPropertyService, musicDirectory.absolutePath,
            removedCoversFile
        ).processFlacFiles()

        JOptionPane.showMessageDialog(null, "Finished")
    } catch (e: Exception) {
        JOptionPane.showMessageDialog(null, "Finished with ERROR\n$e")
    }
}
