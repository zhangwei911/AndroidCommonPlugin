package viz.plugin.android.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jdom.Element

/**
 * @title: NewCommonLibSettings
 * @projectName CodeSearchPlugin
 * @description:
 * @author zhangwei
 * @date 2020/6/30 12:36
 */
@State(name = "NewCommonLibSettings", storages = [(Storage("\$APP_CONFIG$/NewCommonLibFormat.xml"))])
class NewCommonLibSettings : PersistentStateComponent<Element> {
    var rootProjectName = ""
    var groupId = ""
    var artifactId = ""
    var version = ""
    var versionName = ""
    var sdkDir = ""
    var bintrayId = ""
    var bintrayName = ""
    var bintrayApiKey = ""
    var siteUrl = ""
    var gitUrl = ""
    var desc = ""
    var email = ""
    var repo = ""
    var organization = ""
    var gradleDistributionUrl = ""
    var gradlePluginVersion = ""

    override fun getState(): Element? {
        //设置settings显示名称
        val element = Element("NewCommonLibSettings")

        //设置代码保存key-value
        element.setAttribute("rootProjectName", rootProjectName)
        element.setAttribute("groupId", groupId)
        element.setAttribute("artifactId", artifactId)
        element.setAttribute("version", version)
        element.setAttribute("versionName", versionName)
        element.setAttribute("sdkDir", sdkDir)
        element.setAttribute("bintrayId", bintrayId)
        element.setAttribute("bintrayName", bintrayName)
        element.setAttribute("bintrayApiKey", bintrayApiKey)
        element.setAttribute("siteUrl", siteUrl)
        element.setAttribute("gitUrl", gitUrl)
        element.setAttribute("desc", desc)
        element.setAttribute("email", email)
        element.setAttribute("repo", repo)
        element.setAttribute("organization", organization)
        element.setAttribute("gradleDistributionUrl", gradleDistributionUrl)
        element.setAttribute("gradlePluginVersion", gradlePluginVersion)
        return element
    }

    override fun loadState(state: Element) {
        rootProjectName = state.getAttributeValue("rootProjectName")
        groupId = state.getAttributeValue("groupId")
        artifactId = state.getAttributeValue("artifactId")
        version = state.getAttributeValue("version")
        versionName = state.getAttributeValue("versionName")
        sdkDir = state.getAttributeValue("sdkDir")
        bintrayId = state.getAttributeValue("bintrayId")
        bintrayName = state.getAttributeValue("bintrayName")
        bintrayApiKey = state.getAttributeValue("bintrayApiKey")
        siteUrl = state.getAttributeValue("siteUrl")
        gitUrl = state.getAttributeValue("gitUrl")
        desc = state.getAttributeValue("desc")
        email = state.getAttributeValue("email")
        repo = state.getAttributeValue("repo")
        organization = state.getAttributeValue("organization")
        gradleDistributionUrl = state.getAttributeValue("gradleDistributionUrl")
        gradlePluginVersion = state.getAttributeValue("gradlePluginVersion")
    }

    companion object {
        val instance: NewCommonLibSettings
            get() = ServiceManager.getService(NewCommonLibSettings::class.java)
    }
}