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
internal class NameAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
        val application = ApplicationManager.getApplication()
        var androidComponent = application.getComponent(AndroidComponent::class.java)
        val psiFiles =
                FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.allScope(project))
        if (psiFiles.isNotEmpty()) {
            psiFiles.forEach {
                if (it.text.contains("apply plugin: 'com.android.application'")) {
                    if (it.text.contains("apply from:'appName.gradle'")) {
                        println("appName.gradle already reference")
                        return@forEach
                    }
                    val document = PsiDocumentManager.getInstance(project).getDocument(it)
                            ?: return@forEach
                    document.setText(
                            it.text.replace(Regex("android[ ].*\\{"), "apply from:'appName.gradle'\nandroid {\n" +
                                    "    splitABI()\nautoName('test')\n")
                    )
                    FileUtil.createFileFromTemplate("appName.gradle", "appName.gradle", it, project)
                    return@forEach
                }
            }
        }
    }
}