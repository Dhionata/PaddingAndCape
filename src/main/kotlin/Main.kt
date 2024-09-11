import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import utils.FileSelector
import utils.Logger
import java.io.File
import javax.swing.JOptionPane

fun main() {
    val fileSelector = FileSelector()

    val metaFlacFile = fileSelector.selectMetaFlacPath()
    println("Selected metaflac.exe file: ${metaFlacFile.absolutePath}")

    val musicDirectory = fileSelector.selectMusicDirectory()
    println("Selected song directory: ${musicDirectory.absolutePath}")

    val logger = Logger("flac_processing_log.txt")


    val removedCoversFile = File(".", "removed_covers.txt").absolutePath

    val metaFlacService = MetaFlacService(metaFlacFile.absolutePath, logger)
    val shellPropertyService = ShellPropertyService()
    val fileProcessor = FileProcessor(
        metaFlacService, shellPropertyService, musicDirectory.absolutePath, logger,
        removedCoversFile
    )

    fileProcessor.processFlacFiles()

    JOptionPane.showMessageDialog(null, "Finished")
}
