package studio.mandysa.music

import org.junit.Test
import java.io.File
import java.io.FileReader
import java.util.*

class DisablePackageImport {

    @Test
    fun checkup() {
        val path = "${System.getProperty("user.dir")}\\src\\main\\java\\studio\\mandysa\\music"
        exclude(path, "androidx.compose.material3.Text")
    }

    private fun exclude(path: String, key: String) {
        val regex = "(.*)${key}(.*)".toRegex()
        val file = File(path)
        val fs = file.listFiles()
        for (f in fs!!) {
            if (f.isDirectory) {
                exclude(f.path, key)
                continue
            }
            Scanner(FileReader(f)).use { sc ->
                while (sc.hasNextLine()) {
                    val line = sc.nextLine()
                    if (line.matches(regex)) {
                        println("${f}存在${key}")
                    }
                }
            }
        }
    }
}