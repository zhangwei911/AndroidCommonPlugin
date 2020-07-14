package viz.plugin.android

import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream

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
    fun createFileFromTemplate(fileName: String, fileNameNew: String, psiFile: PsiFile, project: Project, replaceList: MutableList<Pair<String, String>> = mutableListOf()) {
        var content = getResources(fileName)
        if (content == null) {
            println("$fileName not exsit")
            return
        }
        replaceList.forEach {
            if (it.first.isNotEmpty()) {
                content = content!!.replace(it.first, it.second)
            }
        }
        val newFile = PsiFileFactory.getInstance(project).createFileFromText(fileNameNew, FileTypes.PLAIN_TEXT, content!!)
        psiFile.parent?.add(newFile)
    }

    fun createFileBatchFromTemplate(fileNamePairs: MutableList<Pair<String, String>>, psiFiles: MutableList<PsiFile>, project: Project, replaceLists: MutableList<MutableList<Pair<String, String>>> = mutableListOf()) {
        if (fileNamePairs.size != psiFiles.size) {
            println("fileNamePairs.size(${fileNamePairs.size}) != psiFiles.size(${psiFiles.size})")
            return
        }
        fileNamePairs.forEachIndexed { index, filePair ->
            if (replaceLists.isEmpty()) {
                createFileFromTemplate(filePair.first, filePair.second, psiFiles[index], project)
            } else {
                createFileFromTemplate(filePair.first, filePair.second, psiFiles[index], project, replaceLists[index])
            }
        }
    }

    /**
     * 解压zip
     */
    fun unzip(zipPath: String, unzipPath: String) {
        val zipFile = File(zipPath)    //压缩文件
        unzipFromInputStream(FileInputStream(zipFile), unzipPath)
    }

    /**
     * 解压zip
     */
    fun unzipFromInputStream(zipInputStream: InputStream, unzipPath: String) {
        val unzipRoot = File(unzipPath)    //解压目录
        val zip = ZipInputStream(zipInputStream)
        var entry = zip.nextEntry
        unzipRoot.mkdir()
        while (entry != null) {
            val current = File(unzipPath, entry.name)
            if (entry.isDirectory) {
                current.mkdirs()
            } else {
                current.parentFile?.mkdirs()
                zip.buffered().copyTo(current.outputStream())
            }
            entry = zip.nextEntry
        }
        zip.closeEntry()
        zipInputStream.close()
    }

    /**
     * 解压zip
     */
    fun unzipFromResources(zipPath: String, unzipPath: String) {
        val inputStreamZip = javaClass.classLoader.getResourceAsStream(zipPath)
        if (inputStreamZip != null) {
            unzipFromInputStream(inputStreamZip, unzipPath)
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    fun renameFile(oldPath: String, newPath: String) {
        val oldFile = File(oldPath)
        val newFile = File(newPath)
        renameFile(oldFile, newFile)
    }

    /**
     * 重命名文件
     *
     * @param oldFile 原来的文件
     * @param newFile 新的文件
     */
    fun renameFile(oldFile: File, newFile: File) {
        //执行重命名
        oldFile.renameTo(newFile)
    }
}