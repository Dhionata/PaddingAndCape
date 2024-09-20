import flac.ShellPropertyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.Logger
import java.io.File
import java.util.Date

class ShellPropertyServiceTest {

    private val shellPropertyService = ShellPropertyService()
    private val filePath: String = TODO("replace to the path of some .flac file")

    @Test
    fun buildShellCommandTest() {
        val result = shellPropertyService.buildShellCommand(
            filePath
        )

        Logger(File("flac_buildShellCommand ${Date().hashCode()}.txt").absolutePath).log(result)
    }

    @Test
    fun getPropertiesFromShellTest() {
        Assertions.assertDoesNotThrow {
            shellPropertyService.getPropertiesFromShell(
                filePath, Logger(
                    File("flac_processing_log_test ${Date().hashCode()}.txt").absolutePath
                )
            )
        }
    }
}
