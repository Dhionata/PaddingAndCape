import flac.ShellPropertyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.Logger
import java.io.File

class ShellPropertyServiceTest {

    val shellPropertyService = ShellPropertyService()
    val filePath: String = TODO()

    @Test
    fun buildShellCommandTest() {
        val result = shellPropertyService.buildShellCommand(
            filePath
        )

        println(result)
    }

    @Test
    fun getPropertiesFromShellTest() {
        Assertions.assertDoesNotThrow {
            shellPropertyService.getPropertiesFromShell(
                filePath, Logger(
                    File("flac_processing_log_test.txt").absolutePath
                )
            )
        }
    }
}
