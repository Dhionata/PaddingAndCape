package flac

import utils.Logger
import utils.Sanitizer
import windows.PowerShell

class ShellPropertyService(private val powerShell: PowerShell = PowerShell()) {

    fun getPropertiesFromShell(filePath: String, logger: Logger): Map<String, String> {

        var filePath = buildShellCommand(filePath)

        logger.log(filePath)

        val output = powerShell.executeCommand(filePath, logger)

        if (output.contains("Title not found") || output.contains("Title property not found") || output.contains
                ("Error accessing file properties")
        ) {
            return mapOf()
        }

        val title = output.substringAfterLast("Title: ")

        return if (title.isNotBlank()) {
            mapOf(Pair("Title", title)).also {
                logger.log(it.entries.toString())
            }
        } else {
            mapOf()
        }
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
                "\$titleIndex = -1;" +
                "for (\$i = 0; \$i -lt 300; \$i++) {" +  // Iterar até o limite de 300
                "\$nomePropriedade = \$folder.GetDetailsOf(\$null, \$i);" +
                "if (\$nomePropriedade -eq 'Title') {" +  // Verifica se a propriedade é "Title"
                "\$titleIndex = \$i;" +
                "break;" +  // Para o loop quando o índice do Title é encontrado
                "}" +
                "}" +
                "if (\$titleIndex -ne -1) {" +  // Se o índice do título foi encontrado
                "\$titleProperty = \$folder.GetDetailsOf(\$file, \$titleIndex);" +
                "if (\$titleProperty -ne '') {" +
                "Write-Output \"Title: \$titleProperty\";" +
                "} else {" +
                "Write-Output 'Title not found';" +
                "}" +
                "} else {" +
                "Write-Output 'Title property not found';" +
                "}" +
                "} catch {" +
                "Write-Host 'Error accessing file properties.'" +
                "}"
    }

}
