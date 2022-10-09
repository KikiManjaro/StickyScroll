package com.github.kikimanjaro.stickyscroll.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service(Service.Level.APP)
@State(name = "Sticky Scroll", storages = [Storage("StickyScroll.xml")])
class StickyScrollConfigService : SimplePersistentStateComponent<StickyScrollConfig>(StickyScrollConfig()) {
	companion object {
		@JvmStatic
		val ConfigInstance: StickyScrollConfigService = ApplicationManager.getApplication().getService(
			StickyScrollConfigService::class.java)
	}
}