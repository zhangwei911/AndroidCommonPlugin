package viz.plugin.android

import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import java.io.*
import java.nio.charset.StandardCharsets

/**
 * @title: FileUtil
 * @projectName AndroidCommonPlugin
 * @description:
 * @author zhangwei
 * @date 2020/5/30 15:22
 */
object FileUtil {
    fun getContent(inputFile: String): String {
        val content = StringBuilder()
        try {
            val bis = BufferedInputStream(FileInputStream(File(inputFile)))
            //10M缓存
            val `in` = BufferedReader(InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024)
            while (`in`.ready()) {
                content.append(`in`.readLine()).append("\n")
            }
            `in`.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return content.toString()
    }

    fun write(content: String, outputFile: String) {
//        if (!File(outputFile).exists()) {
//            File(outputFile).createNewFile()
//        } else {
//            Messages.showMessageDialog(
//                    "文件已存在!" + outputFile,
//                    "提示",
//                    Messages.getInformationIcon()
//            )
//            return
//        }
        var fw: FileWriter? = null
        try {
            fw = FileWriter(outputFile)
            fw.append(content)
            fw.flush()
            fw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getResources(fileName: String): String? {
        val inputStream = javaClass.classLoader.getResourceAsStream(fileName)
        if (inputStream != null) {
            val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
            val br = BufferedReader(reader)
            var line: String?
            line = br.readLine()
            val sb = StringBuilder()
            while (line != null) {
                sb.append(line)
                sb.append("\n")
                line = br.readLine()
            }
            return sb.toString()
        }
        return null
    }

    /**
     * @param fileName 模版文件名
     * @param fileNameNew 新建的文件名
     * @param psiFile 新建文件所在目录下的文件
     * @param project project对象
     */
    fun createFileFromTemplate(fileName: String, fileNameNew: String, psiFile: PsiFile, project: Project) {
        val content = getResources(fileName)
        if (content == null) {
            println("$fileName not exsit")
            return
        }
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(fileNameNew, FileTypes.PLAIN_TEXT, content)
        psiFile.parent?.add(newFile)
    }

    fun createFileBatchFromTemplate(fileNamePairs: MutableList<Pair<String, String>>, psiFiles: MutableList<PsiFile>, project: Project) {
        if (fileNamePairs.size != psiFiles.size) {
            println("fileNamePairs.size(${fileNamePairs.size}) != psiFiles.size(${psiFiles.size})")
            return
        }
        fileNamePairs.forEachIndexed { index, filePair ->
            createFileFromTemplate(filePair.first, filePair.second, psiFiles[index], project)
        }
    }
}