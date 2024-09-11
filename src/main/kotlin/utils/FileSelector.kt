package utils

import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.UIManager.setLookAndFeel
import kotlin.system.exitProcess

class FileSelector {

    init {
        try {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectMetaFlacPath(): File {
        val chooser = JFileChooser()
        chooser.dialogTitle = "Select the metaflac.exe file"
        val result = chooser.showOpenDialog(null)
        return if (result == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile
        } else {
            exitProcess(0)
        }
    }

    fun selectMusicDirectory(): File {
        val chooser = JFileChooser()
        chooser.dialogTitle = "Select the music directory"
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        val result = chooser.showOpenDialog(null)
        return if (result == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile
        } else {
            exitProcess(0)
        }
    }
}
