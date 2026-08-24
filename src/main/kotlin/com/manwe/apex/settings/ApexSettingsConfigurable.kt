package com.manwe.apex.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class ApexSettingsConfigurable : Configurable {
    private var mainPanel: JPanel? = null
    private val lspJarPathField = TextFieldWithBrowseButton()
    private val javaPathField = TextFieldWithBrowseButton()

    override fun getDisplayName(): String = "Apex Language Server"

    override fun createComponent(): JComponent {
        val settings = ApexSettingsState.getInstance()
        lspJarPathField.text = settings.lspJarPath
        javaPathField.text = settings.javaExecutablePath

        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Apex LSP JAR path (apex-jorje-lsp.jar):"), lspJarPathField, 1, false)
            .addLabeledComponent(JBLabel("Java executable path:"), javaPathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        return mainPanel!!
    }

    override fun isModified(): Boolean {
        val settings = ApexSettingsState.getInstance()
        return lspJarPathField.text != settings.lspJarPath || javaPathField.text != settings.javaExecutablePath
    }

    override fun apply() {
        val settings = ApexSettingsState.getInstance()
        settings.lspJarPath = lspJarPathField.text.trim()
        settings.javaExecutablePath = javaPathField.text.trim().ifEmpty { "java" }
    }

    override fun reset() {
        val settings = ApexSettingsState.getInstance()
        lspJarPathField.text = settings.lspJarPath
        javaPathField.text = settings.javaExecutablePath
    }

    override fun disposeUIResources() {
        mainPanel = null
    }
}
