package com.github.kikimanjaro.stickyscroll.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseState
import com.intellij.util.messages.Topic

class StickyScrollConfig : BaseState() {
    var maxLine by property(10)
}

val SettingsChangePublisher = ApplicationManager.getApplication().messageBus.syncPublisher(SettingsChangeListener.TOPIC)

interface SettingsChangeListener {
    fun onHoveringOriginalScrollBarChanged(value: Boolean) {}

    fun refresh(directUpdate: Boolean = false, updateScroll: Boolean = false) {}

    fun onGlobalChanged() {}

    companion object {
        val TOPIC = Topic.create("StickyScrollSettingsChanged", SettingsChangeListener::class.java)
    }
}