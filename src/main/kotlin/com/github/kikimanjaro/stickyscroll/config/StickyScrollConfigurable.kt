package com.github.kikimanjaro.stickyscroll.config

import com.github.kikimanjaro.stickyscroll.message
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*
import com.github.kikimanjaro.stickyscroll.config.StickyScrollConfigService.Companion.ConfigInstance
import org.jetbrains.kotlin.idea.slicer.KotlinSliceUsageCellRenderer.addMouseWheelListener
import java.awt.event.MouseWheelEvent
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import kotlin.math.max
import kotlin.math.min

class StickyScrollConfigurable : BoundSearchableConfigurable("StickyScroll", "com.github.kikimanjaro.stickyscroll") {
    private val config = ConfigInstance.state

    override fun createPanel(): DialogPanel {
        return panel {
            val scrollListener: (e: MouseWheelEvent) -> Unit = {
                val comboBox = it.source as JComboBox<*>
                comboBox.setSelectedIndex(
                    max(
                        0,
                        min(comboBox.selectedIndex + it.wheelRotation, comboBox.itemCount - 1)
                    )
                )
            }
            group(message("settings.general")) {
                row {
                    comboBox(DefaultComboBoxModel(arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
                        .label(message("settings.maxLine"))
                        .bindItem(config::maxLine)
                        .accessibleName(message("settings.maxLine"))
                        .applyToComponent { addMouseWheelListener(scrollListener) }
                }.bottomGap(BottomGap.SMALL)
            }
        }
    }

    override fun apply() {
        super.apply()
        invokeLater { SettingsChangePublisher.onGlobalChanged() }
    }
}