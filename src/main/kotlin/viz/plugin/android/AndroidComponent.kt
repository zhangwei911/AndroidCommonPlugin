package viz.plugin.android

import com.intellij.openapi.components.*
import com.intellij.openapi.ui.Messages
import org.jetbrains.annotations.NotNull

class AndroidComponent : ApplicationComponent {
    override fun initComponent() {
    }

    override fun disposeComponent() {
    }

    @NotNull
    override fun getComponentName(): String {
        return "AndroidComponent"
    }

    fun sayHello() {
        // Show dialog with message
        Messages.showMessageDialog(
            "Hello World!",
            "Sample",
            Messages.getInformationIcon()
        )
    }

    fun generateVersion() {

    }
}