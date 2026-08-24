package com.manwe.apex.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.manwe.apex.settings.ApexSettingsState",
    storages = [Storage("ApexPluginSettings.xml")]
)
@Service(Service.Level.APP)
class ApexSettingsState : PersistentStateComponent<ApexSettingsState> {
    var lspJarPath: String = ""
    var javaExecutablePath: String = "java"

    override fun getState(): ApexSettingsState = this

    override fun loadState(state: ApexSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): ApexSettingsState = service()
    }
}
