package flac

import utils.Logger
import utils.Sanitizer

class ShellPropertyService {
    private val powerShell = PowerShell()

    fun getPropertiesFromShell(filePath: String, logger: Logger): Map<String, String> {
        val properties = mutableMapOf<String, String>()

        var filePath = buildShellCommand(filePath)

        logger.log(filePath)

        val output = powerShell.executeCommand(filePath, logger)

        output.split("\r\n").forEach { line ->
            val parts = line.split(":", limit = 2).map { it.trim() }
            if (parts.size == 2) {
                val (key, value) = parts
                if (value.isNotBlank()) {
                    properties[key] = value
                }
            } else {
                logger.log("Invalid line format: $line")
            }
        }
        properties.remove("PS C")
        properties.forEach { logger.log("${it.key}: ${it.value}") }

        return properties
    }

    fun buildShellCommand(filePath: String): String {
        val filePath = Sanitizer.sanitizeFilePath(filePath)

        return "\$arquivo = \"$filePath\";" +
                "try {" +
                "\$shell = New-Object -ComObject Shell.Application;" +
                "\$pasta = Split-Path \$arquivo;" +
                "\$nomeArquivo = Split-Path \$arquivo -Leaf;" +
                "\$folder = \$shell.Namespace(\$pasta);" +
                "\$file = \$folder.ParseName(\$nomeArquivo);" +
                "for (\$i = 0; \$i -lt 300; \$i++) {" +
                "\$nomePropriedade = \$folder.GetDetailsOf(\$null, \$i);" +
                "\$valorPropriedade = \$folder.GetDetailsOf(\$file, \$i);" +
                "if (\$nomePropriedade -ne '' -and \$valorPropriedade -ne '') {" +
                "Write-Output \"\$nomePropriedade`: \$valorPropriedade\";" +
                "}" +
                "}" +
                "} catch {" +
                "Write-Host 'Error accessing file properties.'" +
                "}"
    }
}
