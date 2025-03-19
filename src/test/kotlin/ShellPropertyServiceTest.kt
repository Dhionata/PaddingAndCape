import flac.ShellPropertyService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ShellPropertyServiceTest {

    private val shellPropertyService = ShellPropertyService()
    private val filePath: String = TODO("replace to the path of some .flac file")

    @Test
    fun buildShellCommandTest() {
        shellPropertyService.buildShellCommand(
            filePath
        )

    }

    @Test
    fun getPropertiesFromShellTest() {
        Assertions.assertDoesNotThrow {
            shellPropertyService.getPropertiesFromShell(filePath)
        }
    }
}
