package viz.plugin.android

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.LocalFileSystem
import viz.plugin.android.settings.NewCommonLibSettings
import java.awt.event.KeyEvent
import java.io.File
import java.nio.charset.StandardCharsets
import javax.swing.*

class NewCommonLibDialog : DialogWrapper {
    private var newCommonLibSettings = NewCommonLibSettings.instance
    private var project: Project
    private lateinit var contentPanel: JPanel
    private lateinit var label_group_id: JLabel
    private lateinit var textField_group_id: JTextField
    private lateinit var label_artifact_id: JLabel
    private lateinit var textField_artifact_id: JTextField
    private lateinit var label_version: JLabel
    private lateinit var textField_version: JTextField
    private lateinit var textField_sdk_dir: JTextField
    private lateinit var textField_bintray_id: JTextField
    private lateinit var textField_bintray_api_key: JTextField
    private lateinit var textField_site_url: JTextField
    private lateinit var textField_git_url: JTextField
    private lateinit var textField_desc: JTextField
    private lateinit var textField_email: JTextField
    private lateinit var textField_repo: JTextField
    private lateinit var textField_organization: JTextField
    private lateinit var textField_version_name: JTextField
    private lateinit var textField_bintray_name: JTextField
    private lateinit var textField_root_project_name: JTextField
    private lateinit var textField_project_dir: JTextField
    private lateinit var textField_gradle_distribution_url: JTextField
    private lateinit var textField_gradle_plugin_version: JTextField
    private lateinit var button_sdk_dir: JButton
    private lateinit var button_project_dir: JButton

    constructor(project: Project) : super(project) {
        title = "New Common Lib"
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
        button_sdk_dir.addActionListener {
            val fd = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            val vf = FileChooser.chooseFile(fd, null, null)
            textField_sdk_dir.text = vf?.path ?: ""
        }
        button_project_dir.addActionListener {
            val fd = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            val vf = FileChooser.chooseFile(fd, null, null)
            textField_project_dir.text = vf?.path ?: ""
        }
        textField_root_project_name.text = this.newCommonLibSettings.rootProjectName
        textField_group_id.text = this.newCommonLibSettings.groupId
        textField_artifact_id.text = this.newCommonLibSettings.artifactId
        textField_version.text = this.newCommonLibSettings.version
        textField_version_name.text = this.newCommonLibSettings.versionName
        textField_sdk_dir.text = this.newCommonLibSettings.sdkDir
        textField_bintray_id.text = this.newCommonLibSettings.bintrayId
        textField_bintray_name.text = this.newCommonLibSettings.bintrayName
        textField_bintray_api_key.text = this.newCommonLibSettings.bintrayApiKey
        textField_site_url.text = this.newCommonLibSettings.siteUrl
        textField_git_url.text = this.newCommonLibSettings.gitUrl
        textField_desc.text = this.newCommonLibSettings.desc
        textField_email.text = this.newCommonLibSettings.email
        textField_repo.text = this.newCommonLibSettings.repo
        textField_organization.text = this.newCommonLibSettings.organization
        textField_gradle_distribution_url.text = this.newCommonLibSettings.gradleDistributionUrl
        textField_gradle_plugin_version.text = this.newCommonLibSettings.gradlePluginVersion
    }

    override fun createCenterPanel(): JComponent? {
        return contentPanel
    }


    override fun doOKAction() {
        if (textField_root_project_name.text.isEmpty() || textField_group_id.text.isEmpty() || textField_artifact_id.text.isEmpty() || textField_version.text.isEmpty() || textField_version_name.text.isEmpty() || textField_sdk_dir.text.isEmpty() || textField_bintray_id.text.isEmpty() || textField_bintray_name.text.isEmpty() || textField_bintray_api_key.text.isEmpty() || textField_site_url.text.isEmpty() || textField_git_url.text.isEmpty() || textField_desc.text.isEmpty() || textField_email.text.isEmpty() || textField_repo.text.isEmpty() || textField_organization.text.isEmpty() || textField_project_dir.text.isEmpty() || textField_gradle_distribution_url.text.isEmpty() || textField_gradle_plugin_version.text.isEmpty()) {
            Messages.showErrorDialog("请确认所有信息均不为空", "错误")
            return
        }
        val application = ApplicationManager.getApplication()
        val projectDir = textField_project_dir.text
        val artifactId = textField_artifact_id.text
        println("start to unzip")
        FileUtil.unzipFromResources("NewCommonLib.zip", projectDir)
        println("unzip finished")
        println("start to replace")
        val replaceFileList = mutableListOf(
                "settings.gradle",
                "local.properties",
                "build.gradle",
                "README.md",
                "gradle/wrapper/gradle-wrapper.properties",
                "newCommonLib/maven-info.gradle",
                "newCommonLib/maven-info.properties",
                "newCommonLib/bintray.properties"
        )
        val keyList = mutableListOf(
                Pair("{ROOT_PROJECT_NAME}", textField_root_project_name.text),
                Pair("{LIB_VERSION}", textField_version.text),
                Pair("{ARTIFACT_ID}", textField_artifact_id.text),
                Pair("{GROUP_ID}", textField_group_id.text),
                Pair("{SITE_URL}", textField_site_url.text),
                Pair("{GIT_URL}", textField_git_url.text),
                Pair("{DESC}", textField_desc.text),
                Pair("{BINTRAY_ID}", textField_bintray_id.text),
                Pair("{BINTRAY_NAME}", textField_bintray_name.text),
                Pair("{BINTRAY_APIKEY}", textField_bintray_api_key.text),
                Pair("{EMAIL}", textField_email.text),
                Pair("{REPO}", textField_repo.text),
                Pair("{ORGANIZATION}", textField_organization.text),
                Pair("{LIB_VERSION_NAME}", textField_version_name.text),
                Pair("{GRADLE_DISTRIBUTION_URL}", textField_gradle_distribution_url.text),
                Pair("{GRADLE_PLUGIN_VERSION}", textField_gradle_plugin_version.text),
                Pair("{SDK_DIR}", textField_sdk_dir.text)
        )
        application.runWriteAction {
            var count = 0
            replaceFileList.forEachIndexed { index, filePath ->
                val filePath = projectDir + if (projectDir.endsWith("/")) {
                    ""
                } else {
                    "/"
                } + filePath
                val vf = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath)
                println(filePath + if (vf != null) {
                    ""
                } else {
                    " not exist!"
                })
                if (vf != null) {
                    val document = FileDocumentManager.getInstance().getDocument(vf)
                    if (document == null) {
                        count++
                        checkFinish(count, replaceFileList, projectDir, artifactId)
                        return@forEachIndexed
                    }
                    var content = document.text
                    keyList.forEach { keyPair ->
                        content = content.replace(keyPair.first, String(keyPair.second.toByteArray(), StandardCharsets.UTF_8))
                    }
                    FileUtil.write(content, filePath)
                }
                count++
                checkFinish(count, replaceFileList, projectDir, artifactId)
            }
            println("replace finished")
        }
    }

    private fun checkFinish(count: Int, replaceFileList: MutableList<String>, projectDir: String, artifactId: String) {
        if (count == replaceFileList.size) {
            renameFile(projectDir, artifactId)
        }
    }

    private fun renameFile(projectDir: String, artifactId: String) {
        println("start to rename newCommonLib Dir")
        FileUtil.renameFile(File(projectDir, "newCommonLib"), File(projectDir, artifactId))
        println("rename newCommonLib Dir finished")
        super.doOKAction()
        Messages.showInfoMessage("新建库项目成功!\n请点击菜单File-open,选择${projectDir}打开项目", "提示")
    }

    override fun doCancelAction() {
        super.doCancelAction()
    }

    override fun doValidate(): ValidationInfo? {
        return super.doValidate()
    }
}