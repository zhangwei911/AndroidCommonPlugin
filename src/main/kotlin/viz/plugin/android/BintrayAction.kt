package viz.plugin.android

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope

/**
 * @author zhangwei
 * @title: BintrayAction
 * @projectName AndroidCommonPlugin
 * @description:
 * @date 2020/5/30 15:16
 */
internal class BintrayAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project as Project
        val dialog = SelectModuleDialog(project, 1)
        dialog.show()
    }
}