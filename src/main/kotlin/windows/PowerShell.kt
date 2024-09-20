package windows

import utils.Logger
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class PowerShell(
    private val processBuilder: ProcessBuilder = ProcessBuilder(
        "powershell.exe", "-NoProfile", "-nologo"
    ).redirectErrorStream(true)
) {

    fun executeCommand(command: String, logger: Logger): String {
        val process = processBuilder.start()

        OutputStreamWriter(process.outputStream).use {
            it.write(command)
            it.flush()
        }

        val output = readStream(process.inputStream)

        val errorOutput = readStream(process.errorStream)

        if (errorOutput.isNotBlank()) {
            logger.log("\nError executing PowerShell command: $errorOutput\n")
        }

        process.waitFor()

        return output.substringAfterLast("{Write-Host 'Error accessing file properties.'}").substringBefore("\r\n")
    }

    private fun readStream(stream: InputStream): String {
        return BufferedReader(InputStreamReader(stream)).use { it.readText() }
    }
}
