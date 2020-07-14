package viz.plugin.android.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * @title: NewCommonLibSettingsConfigurable
 * @projectName CodeSearchPlugin
 * @description:
 * @author zhangwei
 * @date 2020/6/30 12:29
 */
class NewCommonLibSettingsConfigurable : Configurable {
    private var newCommonLibSettings = NewCommonLibSettings.instance
    private var settingsForm: Settings? = null

    @Nls
    override fun getDisplayName(): String {
        return "New Common Lib Settings"
    }

    override fun isModified(): Boolean {
        return this.newCommonLibSettings.rootProjectName != this.settingsForm?.textField_root_project_name?.text ?: "" ||
                this.newCommonLibSettings.groupId != this.settingsForm?.textField_group_id?.text ?: "" ||
                this.newCommonLibSettings.artifactId != this.settingsForm?.textField_artifact_id?.text ?: "" ||
                this.newCommonLibSettings.version != this.settingsForm?.textField_version?.text ?: "" ||
                this.newCommonLibSettings.versionName != this.settingsForm?.textField_version_name?.text ?: "" ||
                this.newCommonLibSettings.sdkDir != this.settingsForm?.textField_sdk_dir?.text ?: "" ||
                this.newCommonLibSettings.bintrayId != this.settingsForm?.textField_bintray_id?.text ?: "" ||
                this.newCommonLibSettings.bintrayName != this.settingsForm?.textField_bintray_name?.text ?: "" ||
                this.newCommonLibSettings.bintrayApiKey != this.settingsForm?.textField_bintray_api_key?.text ?: "" ||
                this.newCommonLibSettings.siteUrl != this.settingsForm?.textField_site_url?.text ?: "" ||
                this.newCommonLibSettings.gitUrl != this.settingsForm?.textField_git_url?.text ?: "" ||
                this.newCommonLibSettings.desc != this.settingsForm?.textField_desc?.text ?: "" ||
                this.newCommonLibSettings.email != this.settingsForm?.textField_email?.text ?: "" ||
                this.newCommonLibSettings.repo != this.settingsForm?.textField_repo?.text ?: "" ||
                this.newCommonLibSettings.organization != this.settingsForm?.textField_organization?.text ?: "" ||
                this.newCommonLibSettings.gradleDistributionUrl != this.settingsForm?.textField_gradle_distribution_url?.text ?: "" ||
                this.newCommonLibSettings.gradlePluginVersion != this.settingsForm?.textField_gradle_plugin_version?.text ?: ""
    }

    override fun apply() {
        this.newCommonLibSettings.rootProjectName = this.settingsForm?.textField_root_project_name?.text?:""
        this.newCommonLibSettings.groupId = this.settingsForm?.textField_group_id?.text?:""
        this.newCommonLibSettings.artifactId = this.settingsForm?.textField_artifact_id?.text?:""
        this.newCommonLibSettings.version = this.settingsForm?.textField_version?.text?:""
        this.newCommonLibSettings.versionName = this.settingsForm?.textField_version_name?.text?:""
        this.newCommonLibSettings.sdkDir = this.settingsForm?.textField_sdk_dir?.text?:""
        this.newCommonLibSettings.bintrayId = this.settingsForm?.textField_bintray_id?.text?:""
        this.newCommonLibSettings.bintrayName = this.settingsForm?.textField_bintray_name?.text?:""
        this.newCommonLibSettings.bintrayApiKey = this.settingsForm?.textField_bintray_api_key?.text?:""
        this.newCommonLibSettings.siteUrl = this.settingsForm?.textField_site_url?.text?:""
        this.newCommonLibSettings.gitUrl = this.settingsForm?.textField_git_url?.text?:""
        this.newCommonLibSettings.desc = this.settingsForm?.textField_desc?.text?:""
        this.newCommonLibSettings.email = this.settingsForm?.textField_email?.text?:""
        this.newCommonLibSettings.repo = this.settingsForm?.textField_repo?.text?:""
        this.newCommonLibSettings.organization = this.settingsForm?.textField_organization?.text?:""
        this.newCommonLibSettings.gradleDistributionUrl = this.settingsForm?.textField_gradle_distribution_url?.text?:""
        this.newCommonLibSettings.gradlePluginVersion = this.settingsForm?.textField_gradle_plugin_version?.text?:""
    }

    override fun reset() {
        this.settingsForm?.textField_root_project_name?.text = this.newCommonLibSettings.rootProjectName
        this.settingsForm?.textField_group_id?.text = this.newCommonLibSettings.groupId
        this.settingsForm?.textField_artifact_id?.text = this.newCommonLibSettings.artifactId
        this.settingsForm?.textField_version?.text = this.newCommonLibSettings.version
        this.settingsForm?.textField_version_name?.text = this.newCommonLibSettings.versionName
        this.settingsForm?.textField_sdk_dir?.text = this.newCommonLibSettings.sdkDir
        this.settingsForm?.textField_bintray_id?.text = this.newCommonLibSettings.bintrayId
        this.settingsForm?.textField_bintray_name?.text = this.newCommonLibSettings.bintrayName
        this.settingsForm?.textField_bintray_api_key?.text = this.newCommonLibSettings.bintrayApiKey
        this.settingsForm?.textField_site_url?.text = this.newCommonLibSettings.siteUrl
        this.settingsForm?.textField_git_url?.text = this.newCommonLibSettings.gitUrl
        this.settingsForm?.textField_desc?.text = this.newCommonLibSettings.desc
        this.settingsForm?.textField_email?.text = this.newCommonLibSettings.email
        this.settingsForm?.textField_repo?.text = this.newCommonLibSettings.repo
        this.settingsForm?.textField_organization?.text = this.newCommonLibSettings.organization
        this.settingsForm?.textField_gradle_distribution_url?.text = this.newCommonLibSettings.gradleDistributionUrl
        this.settingsForm?.textField_gradle_plugin_version?.text = this.newCommonLibSettings.gradlePluginVersion
    }

    override fun createComponent(): JComponent? {
        if (null == this.settingsForm) {
            this.settingsForm = Settings()
        }
        val mainPanel = this.settingsForm!!.contentPanel

        return mainPanel
    }

}