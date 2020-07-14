package viz.plugin.android.settings

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * @author zhangwei
 * @title: Settings
 * @projectName AndroidCommonPlugin
 * @description:
 * @date 2020/7/10 15:24
 */
class Settings {
    lateinit var contentPanel: JPanel
    lateinit var label_group_id: JLabel
    lateinit var textField_group_id: JTextField
    lateinit var label_artifact_id: JLabel
    lateinit var textField_artifact_id: JTextField
    lateinit var label_version: JLabel
    lateinit var textField_version: JTextField
    lateinit var textField_sdk_dir: JTextField
    lateinit var textField_bintray_id: JTextField
    lateinit var textField_bintray_api_key: JTextField
    lateinit var textField_site_url: JTextField
    lateinit var textField_git_url: JTextField
    lateinit var textField_desc: JTextField
    lateinit var textField_email: JTextField
    lateinit var textField_repo: JTextField
    lateinit var textField_organization: JTextField
    lateinit var textField_version_name: JTextField
    lateinit var textField_bintray_name: JTextField
    lateinit var textField_root_project_name: JTextField
    lateinit var textField_gradle_distribution_url: JTextField
    lateinit var textField_gradle_plugin_version: JTextField
    lateinit var button_sdk_dir: JButton

    init {
        button_sdk_dir.addActionListener {
            val fd = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            val vf = FileChooser.chooseFile(fd, null, null)
            textField_sdk_dir.text = vf?.path ?: ""
        }
    }
}