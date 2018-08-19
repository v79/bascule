package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import spark.ModelAndView
import spark.kotlin.get

@SparkController
class HomeController : AbstractController(path = "/") {

	val templateService = DBTemplateService()
	val pageService = DBPageService()

	init {

		get(path) {

			val topPages = pageService.listPages(10)
			val topTemplates = templateService.listPageTemplates(10)
			model.put("pages", topPages)
			model.put("templates", topTemplates)

			debugModel()
			engine.render(ModelAndView(model,"home"))
		}

		get("$path/other") {
			debugModel()
			engine.render(ModelAndView(model,"other"))
		}

	}

}

class PageForm(val refName: String, val title: String, val templateRef: String)