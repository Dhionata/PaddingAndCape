import flac.FileProcessor
import flac.MetaFlacService
import flac.ShellPropertyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths
import java.util.*
import javax.swing.JOptionPane

class MainTest {
    val metaFlacFile: File = File("metaflac.exe")
    val musicDirectory: File = File(Paths.get(System.getProperty("user.home"), "Music").toString())

    @Test
    fun mainTest() {
        Assertions.assertDoesNotThrow {
            try {
                val removedCoversFile = File("removed_covers_test ${Date().hashCode()}.txt")

                val metaFlacService = MetaFlacService(metaFlacFile.absolutePath)
                val shellPropertyService = ShellPropertyService()

                FileProcessor(
                    metaFlacService, shellPropertyService, musicDirectory.absolutePath,
                    removedCoversFile.absolutePath
                ).processFlacFiles()

                JOptionPane.showMessageDialog(null, "Finished")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
