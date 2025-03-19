package windows

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.logging.Logger

class PowerShell(
    private val processBuilder: ProcessBuilder = ProcessBuilder(
        "powershell.exe", "-NoProfile", "-nologo"
    ).redirectErrorStream(true),
) {

    private val logger = Logger.getLogger(this.javaClass.name)

    fun executeCommand(command: String): String {
        val process = processBuilder.start()

        OutputStreamWriter(process.outputStream).use {
            it.write(command)
            it.flush()
        }

        val output = readStream(process.inputStream)

        process.waitFor()

        val outputFiltered = output.substringAfterLast("{Write-Host 'Error accessing file properties.'}").substringBefore("\r\n")

        logger.info(outputFiltered)

        return outputFiltered
    }

    private fun readStream(stream: InputStream): String {
        return BufferedReader(InputStreamReader(stream)).use { it.readText() }
    }
}
