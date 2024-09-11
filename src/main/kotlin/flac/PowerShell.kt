package flac

import utils.Logger
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class PowerShell {

    fun executeCommand(command: String, logger: Logger): String {
        val processBuilder = ProcessBuilder("powershell.exe", "-NoProfile", "-nologo")
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        // Write the command to the PowerShell process
        val outputStreamWriter = OutputStreamWriter(process.outputStream)
        outputStreamWriter.use {
            it.write(command)
            it.flush()
        }

        // Read the output and error streams
        val output = readStream(process.inputStream)
        val errorOutput = readStream(process.errorStream)

        if (errorOutput.isNotBlank()) {
            // Log any errors encountered during command execution
            logger.log("\nError executing PowerShell command: $errorOutput\n")
        }

        process.waitFor()
        return output
    }

    private fun readStream(stream: InputStream): String {
        return BufferedReader(InputStreamReader(stream)).use { it.readText() }
    }
}
