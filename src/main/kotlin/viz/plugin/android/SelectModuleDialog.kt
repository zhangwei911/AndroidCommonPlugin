package viz.plugin.android

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import java.awt.event.KeyEvent
import javax.swing.*

class SelectModuleDialog : DialogWrapper {
    private var project: Project? = null
    private var contentPanel: JPanel? = null
    private var comboBox_modules: JComboBox<String>? = null
    private var label_group_id: JLabel? = null
    private var textField_group_id: JTextField? = null
    private var label_artifact_id: JLabel? = null
    private var textField_artifact_id: JTextField? = null
    private var label_version: JLabel? = null
    private var textField_version: JTextField? = null
    private var psiFiles: MutableList<PsiFile> = mutableListOf()

    constructor(project: Project) : super(project) {
        title = "Select Module"
        this.project = project
        init()
    }

    override fun init() {
        super.init()
        // call onCancel() on ESCAPE
        contentPanel?.registerKeyboardAction(
                { },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        )

        var moduleSourceRoot = ProjectRootManager.getInstance(this.project!!).fileIndex
        var projectName = project!!.name
        var vFiles = ProjectRootManager.getInstance(project!!).contentSourceRoots

        psiFiles = FilenameIndex.getFilesByName(project!!, "settings.gradle", GlobalSearchScope.allScope(project!!))
                .toMutableList()
        if (psiFiles.isEmpty()) {
            Messages.showMessageDialog(
                    "The project has no settings.gradle",
                    "Tips",
                    Messages.getInformationIcon()
            )
        } else {
            comboBox_modules?.removeAllItems()
            psiFiles.forEach {
                val regex = Regex("include ':([a-zA-Z\\-_0-9].*)'")
                regex.findAll(it.text).forEach { mr ->
                    mr.groups[1]?.apply {
                        comboBox_modules?.addItem(value)
                    }
                }
            }
            if (comboBox_modules?.itemCount == 0) {
                Messages.showMessageDialog(
                        "The project has no module",
                        "Tips",
                        Messages.getErrorIcon()
                )
                close(0)
            }
        }
    }

    override fun createCenterPanel(): JComponent? {
        return contentPanel
    }

    override fun doOKAction() {
        val groupId = textField_group_id?.text
        if (groupId.isNullOrEmpty()) {
            Messages.showMessageDialog("请输入GROUP ID", "提示", Messages.getErrorIcon())
            return
        }
        val artifaceId = textField_artifact_id?.text
        if (artifaceId.isNullOrEmpty()) {
            Messages.showMessageDialog("请输入ARTIFACE ID", "提示", Messages.getErrorIcon())
            return
        }
        val version = textField_version?.text
        if (version.isNullOrEmpty()) {
            Messages.showMessageDialog("请输入VERSION", "提示", Messages.getErrorIcon())
            return
        }
        comboBox_modules?.apply {
            val psiFilesBuildGradle =
                    FilenameIndex.getFilesByName(project!!, "build.gradle", GlobalSearchScope.allScope(project!!))
            val psiFile =
                    psiFilesBuildGradle.first { it.virtualFile.path == project!!.basePath + "/$selectedItem/build.gradle" }
            if (psiFile != null) {
                val rootBuildGradle = LocalFileSystem.getInstance().findFileByPath(project!!.basePath + "/build.gradle")
                if (rootBuildGradle != null) {
                    val rootBuildGradlePsiFile = PsiManager.getInstance(project!!).findFile(rootBuildGradle)
                    if (rootBuildGradlePsiFile != null) {
                        if (rootBuildGradlePsiFile.text.contains("getRepositoryDir")) {
                            println("本地库地址已添加")
                        } else {
                            val document = PsiDocumentManager.getInstance(project!!).getDocument(rootBuildGradlePsiFile)
                                    ?: return
                            document.setText(
                                    rootBuildGradlePsiFile.text.replace(
                                            Regex("allprojects[ ].*\\{\n[ ].*repositories[ ].*\\{"), "allprojects {\n" +
                                            "    repositories {\nmaven{url getRepositoryDir()}\n"
                                    ) + FileUtil.getResources("localRepo.txt")
                            )
                        }
                    } else {
                        println("根目录没有build.gradle")
                        Messages.showMessageDialog(
                                "The project root has no build.gradle",
                                "Tips",
                                Messages.getErrorIcon()
                        )
                    }
                } else {
                    println("根目录没有build.gradle")
                    Messages.showMessageDialog(
                            "The project root has no build.gradle",
                            "Tips",
                            Messages.getErrorIcon()
                    )
                }
                if (psiFile.text.contains("com.android.library")) {
                    if (psiFile.text.contains("apply from:'maven.gradle'")) {
                        println("maven.gradle already reference")
                        Messages.showMessageDialog(
                                "The module maven.gradle already reference",
                                "Tips",
                                Messages.getWarningIcon()
                        )
                        return
                    }
                    val document = PsiDocumentManager.getInstance(project!!).getDocument(psiFile)
                    if (document != null) {
                        document.setText(
                                psiFile.text.replace(Regex("android[ ].*\\{"), "apply from:'maven.gradle'\nandroid {\n")
                        )
                        FileUtil.createFileBatchFromTemplate(
                                mutableListOf(
                                        Pair("maven.gradle", "maven.gradle"),
                                        Pair("maven-info.gradle", "maven-info.gradle"),
                                        Pair("maven-info.properties", "maven-info.properties")
                                ), mutableListOf(psiFile, psiFile, psiFile), project!!,
                                mutableListOf(mutableListOf(
                                        Pair("", "")
                                ),mutableListOf(
                                        Pair("viz.commonlib", groupId),
                                        Pair("test", artifaceId),
                                        Pair("1.0.0", version)
                                ),mutableListOf(
                                        Pair("viz.commonlib", groupId),
                                        Pair("test", artifaceId),
                                        Pair("1.0.0", version)
                                ))
                        )
                    } else {
                        println("获取document失败")
                        Messages.showMessageDialog(
                                "get document failed",
                                "Tips",
                                Messages.getErrorIcon()
                        )
                    }
                } else {
                    println("该模块不是库")
                    Messages.showMessageDialog(
                            "The module is not library",
                            "Tips",
                            Messages.getErrorIcon()
                    )
                }
            } else {
                println("该模块没有build.gradle")
                Messages.showMessageDialog(
                        "The module has no build.gradle",
                        "Tips",
                        Messages.getErrorIcon()
                )
            }
        }
        super.doOKAction()
    }

    override fun doCancelAction() {
        super.doCancelAction()
    }

    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}