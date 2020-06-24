package viz.plugin.android

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author zhangwei
 * @title: AndroidAction
 * @projectName AndroidCommonPlugin
 * @description:
 * @date 2020/5/30 15:16
 */
internal class BintrayAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
        val application = ApplicationManager.getApplication()
        var androidComponent = application.getComponent(AndroidComponent::class.java)
        val psiFiles =
            FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.allScope(project))
        if (psiFiles.isNotEmpty()) {
            psiFiles.forEach {
                if (it.text.contains("apply plugin: 'com.android.library'")) {
                    if (it.text.contains("apply from:'bintray.gradle'")) {
                        println("bintray.gradle already reference")
                        return@forEach
                    }
                    val document = PsiDocumentManager.getInstance(project).getDocument(it)
                        ?: return@forEach
                    document.setText(
                        it.text.replace(
                            Regex("android[ ].*\\{"),
                            "apply from:'bintray.gradle'\nandroid {\n"
                        ) + "\npackageTask()"
                    )
                    FileUtil.createFileBatchFromTemplate(
                        mutableListOf(
                            Pair("bintray.gradle", "bintray.gradle"),
                            Pair("bintray.properties", "bintray.properties")
                        ), mutableListOf(it, it), project
                    )
                    return@forEach
                } else if (it.text.contains(Regex("buildscript[ ].*\\{"))) {
                    if (it.text.contains("getRepositoryDir")) {
                        println("本地库地址已添加")
                        return@forEach
                    }
                    val document = PsiDocumentManager.getInstance(project).getDocument(it)
                        ?: return@forEach
                    document.setText(
                        it.text.replace(
                            Regex("classpath 'com.android.tools.build:gradle:[a-zA-Z0-9.\\-_]'"), "$1\n" +
                                    "        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'\n" +
                                    "        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'"
                        )
                    )
                }
            }
        }
    }
}