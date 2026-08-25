package com.manwe.apex.lsp

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.ide.plugins.cl.PluginAwareClassLoader
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor
import com.manwe.apex.settings.ApexSettingsConfigurable
import com.manwe.apex.settings.ApexSettingsState
import java.io.File

class ApexLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Apex") {

    override fun isSupportedFile(file: VirtualFile): Boolean {
        return file.extension in listOf("cls", "trigger", "apex")
    }

    override fun createCommandLine(): GeneralCommandLine {
        val settings = ApexSettingsState.getInstance()
        val javaExec = settings.javaExecutablePath.ifEmpty { "java" }
        val jarPath = resolveLspJarPath(settings.lspJarPath)

        if (jarPath == null) {
            notifyMissingJar(project)
            throw IllegalStateException("Apex Language Server JAR (apex-jorje-lsp.jar) not found.")
        }

        return GeneralCommandLine(javaExec, "-jar", jarPath).apply {
            project.basePath?.let { withWorkDirectory(it) }
            withCharset(Charsets.UTF_8)
        }
    }

    private fun resolveLspJarPath(configuredPath: String): String? {
        // 1. Explicitly configured path in settings
        if (configuredPath.isNotBlank()) {
            val file = File(configuredPath)
            if (file.exists()) return file.absolutePath
        }

        // 2. Local dev project path (lib/apex-jorje-lsp.jar in current project or user home)
        val userHome = System.getProperty("user.home")
        if (userHome != null) {
            val devLib = File(userHome, "IdeaProjects/apexPlugin/lib/apex-jorje-lsp.jar")
            if (devLib.exists()) return devLib.absolutePath
        }

        project.basePath?.let { basePath ->
            val localLib = File(basePath, "lib/apex-jorje-lsp.jar")
            if (localLib.exists()) return localLib.absolutePath
        }

        // 3. Installed plugin directory
        try {
            val classLoader = ApexLspServerDescriptor::class.java.classLoader
            val pluginDescriptor = (classLoader as? PluginAwareClassLoader)?.pluginDescriptor
            val pluginDir = pluginDescriptor?.pluginPath?.toFile() ?: PathManager.getJarForClass(ApexLspServerDescriptor::class.java)?.toFile()
            if (pluginDir != null && pluginDir.exists()) {
                val searchDir = if (pluginDir.isFile) pluginDir.parentFile?.parentFile else pluginDir
                if (searchDir != null && searchDir.exists()) {
                    val foundInPlugin = searchDir.walkTopDown().maxDepth(5).firstOrNull { it.name == "apex-jorje-lsp.jar" }
                    if (foundInPlugin != null && foundInPlugin.exists()) {
                        return foundInPlugin.absolutePath
                    }
                }
            }
        } catch (_: Exception) {}

        // 4. Bundled resource extraction
        val extractedPath = extractBundledJar()
        if (extractedPath != null) return extractedPath

        // 5. Check environment variables
        val envPath = System.getenv("APEX_LSP_JAR") ?: System.getenv("SALESFORCE_APEX_LSP_JAR")
        if (!envPath.isNullOrBlank()) {
            val envFile = File(envPath)
            if (envFile.exists()) return envFile.absolutePath
        }

        // 6. Check system property
        val sysPropPath = System.getProperty("apex.lsp.jar")
        if (!sysPropPath.isNullOrBlank()) {
            val sysFile = File(sysPropPath)
            if (sysFile.exists()) return sysFile.absolutePath
        }

        // 7. Check extension directories across IDEs & editors
        if (userHome != null) {
            val userHomeFile = File(userHome)
            val searchDirs = listOf(
                File(userHomeFile, ".vscode/extensions"),
                File(userHomeFile, ".vscode-server/extensions"),
                File(userHomeFile, ".cursor/extensions"),
                File(userHomeFile, ".codium/extensions"),
                File(userHomeFile, ".local/share/code-server/extensions"),
                File(userHomeFile, ".sfdx"),
                File(userHomeFile, ".sf")
            )

            for (extDir in searchDirs) {
                if (extDir.exists() && extDir.isDirectory) {
                    val salesforceExts = extDir.listFiles { _, name ->
                        name.startsWith("salesforce.salesforcedx-vscode-apex") || name.contains("apex")
                    }
                    salesforceExts?.forEach { dir ->
                        val found = dir.walkTopDown().maxDepth(5).firstOrNull { it.name == "apex-jorje-lsp.jar" }
                        if (found != null && found.exists()) {
                            return found.absolutePath
                        }
                    }
                }
            }
        }

        return null
    }

    private fun extractBundledJar(): String? {
        try {
            val stream = ApexLspServerDescriptor::class.java.getResourceAsStream("/lib/apex-jorje-lsp.jar")
                ?: ApexLspServerDescriptor::class.java.getResourceAsStream("/apex-jorje-lsp.jar")
                ?: return null

            val targetDir = File(PathManager.getTempPath(), "apex-lsp")
            if (!targetDir.exists()) targetDir.mkdirs()
            val targetFile = File(targetDir, "apex-jorje-lsp.jar")

            if (!targetFile.exists() || targetFile.length() == 0L) {
                stream.use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            } else {
                stream.close()
            }

            if (targetFile.exists() && targetFile.length() > 0) {
                return targetFile.absolutePath
            }
        } catch (_: Exception) {}
        return null
    }

    private fun notifyMissingJar(project: Project) {
        try {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Apex Plugin Notifications")
                ?.createNotification(
                    "Apex Language Server Not Found",
                    "Could not locate 'apex-jorje-lsp.jar'. Please specify its path in Settings -> Apex Language Server.",
                    NotificationType.WARNING
                )
                ?.addAction(NotificationAction.createSimple("Open Settings...") {
                    ShowSettingsUtil.getInstance().showSettingsDialog(project, ApexSettingsConfigurable::class.java)
                })
                ?.notify(project)
        } catch (_: Exception) {
            // Ignore notification error if IDE UI frame is unavailable
        }
    }
}



