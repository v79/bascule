package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.ModelAndView
import spark.kotlin.get

@SparkController
class HomeController : AbstractController(path = "/") {

	val templateService = DBTemplateService()
	val pageService = DBPageService()

	override val logger: Logger = LoggerFactory.getLogger(this.javaClass)

	init {

//		before("$path/*") {
//			logger.info(request.pathInfo())
//			model.put("__mode", Mode.VIEW) // default to viewing
//			model.put("__title","Bascule CMS")
//		}

		get(path) {

			val topPages = pageService.listPages(10)
			val topTemplates = templateService.listPageTemplates(10)
			model.put("pages", topPages)
			model.put("templates", topTemplates)

			engine.render(ModelAndView(model,"home"))
		}

		get("$path/other") {
			engine.render(ModelAndView(model,"other"))
		}

	}

}

class PageForm(val refName: String, val title: String, val templateRef: String)