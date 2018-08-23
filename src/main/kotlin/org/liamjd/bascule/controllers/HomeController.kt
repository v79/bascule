package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.ModelAndView
import spark.kotlin.get


@SparkController
class HomeController : AbstractController() {

	val templateService = DBTemplateService()
	val pageService = DBPageService()

	override val logger: Logger = LoggerFactory.getLogger(this.javaClass)

	init {

		// need to test that I can handle before("/") without triggering other before calls
		// before() - matches everything - too eager
		// before("/") - only matched /
		// before("/*") - matched /page/new, /pageTemplate/new, /page/edit/* - too eager
		// path("/") { before() { ... } } - nothing is caught. path() doesn't work at all...
		// but is this necessary? no authentication is required for HomeController.
		// In fact, HomeController should only respond to "/"...

		get("/") {

			model.put("__mode", Mode.VIEW) // default to viewing
			model.put("__title", "Bascule CMS")

			val topPages = pageService.listPages(10)
			val topTemplates = templateService.listPageTemplates(10)
			model.put("pages", topPages)
			model.put("templates", topTemplates)

			engine.render(ModelAndView(model,"home"))
		}

		get("/other") {
			engine.render(ModelAndView(model,"other"))
		}

	}

}

class PageForm(val refName: String, val title: String, val templateRef: String)