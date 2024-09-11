import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import org.junit.jupiter.api.Test
import utils.Logger
import java.io.File
import java.nio.file.Paths

class MainTest {

    @Test
    fun mainTest() {
        val metaFlacFile = File("metaflac.exe")
        val musicDirectory: File = File(Paths.get(System.getProperty("user.home"), "Music").toString())

        val logger = Logger("flac_processing_log_test.txt")

        val removedCoversFile = File("removed_covers_test.txt")

        val metaFlacService = MetaFlacService(metaFlacFile.absolutePath, logger)
        val shellPropertyService = ShellPropertyService()

        val fileProcessor = FileProcessor(
            metaFlacService, shellPropertyService, musicDirectory.absolutePath, logger,
            removedCoversFile.absolutePath
        )

        fileProcessor.processFlacFiles()
    }

}
