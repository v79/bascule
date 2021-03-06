package org.liamjd.web.model

import org.liamjd.bascule.render.HandlebarsRenderService
import org.liamjd.bascule.render.RenderMode

class Page (val refName: String, var title: String) {
	var templateRef: String = ""
	var blocks = mutableListOf<Block>()
	var blockGroups = mutableListOf<BlockGroup>()

	override fun toString(): String {
		return "Page '$refName' [title=$title, block count=${blocks.size}, block group count=${blockGroups.size}"
	}

	fun getGroup(name: String): BlockGroup?  {
		return blockGroups.find { it.refName == name }
	}

	fun render(): String {
		val renderService = HandlebarsRenderService()
		return renderService.render(this, RenderMode.EDIT)
	}
}
