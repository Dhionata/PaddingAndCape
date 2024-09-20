import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.Logger
import java.io.File
import java.nio.file.Paths
import java.util.Date
import javax.swing.JOptionPane

class MainTest {
    val metaFlacFile = File("metaflac.exe")
    val musicDirectory: File = File(Paths.get(System.getProperty("user.home"), "Music").toString())

    @Test
    fun mainTest() {
        Assertions.assertDoesNotThrow {
            try {
                val logger = Logger("flac_processing_log_test ${Date().hashCode()}.txt")

                val removedCoversFile = File("removed_covers_test ${Date().hashCode()}.txt")

                val metaFlacService = MetaFlacService(metaFlacFile.absolutePath, logger)
                val shellPropertyService = ShellPropertyService()

                val fileProcessor = FileProcessor(
                    metaFlacService, shellPropertyService, musicDirectory.absolutePath, logger,
                    removedCoversFile.absolutePath
                )

                fileProcessor.processFlacFiles()

                JOptionPane.showMessageDialog(null, "Finished")
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(null, "Finished with ERROR\n$e")
            }
        }
    }
}
