package com.github.kikimanjaro.stickyscroll.services

import com.intellij.openapi.project.Project
import com.github.kikimanjaro.stickyscroll.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
