package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.caisson.extensions.bind
import org.liamjd.web.model.Page
import spark.ModelAndView
import spark.kotlin.before
import spark.kotlin.get
import spark.kotlin.post

@SparkController
class PageController : AbstractController() {

	val templateService = DBTemplateService()
	val pageService = DBPageService()

	init {

		before("/page/*") {
			println("Before PageController ${request.pathInfo()}")
		}

		get("/page/new") {
			val templates = templateService.listPageTemplates(10)

			model.put("__title", "New page")

			if (templates.isNotEmpty()) {
				model.put("__pageTemplates", templates)

				engine.render(ModelAndView(model, "page/new"))
			} else {
				"no templates!"
			}
		}

		post("page/new/create") {
			val form = request.bind<PageForm>()
			val page: Page?
			if (form != null) {
				page = Page(form.refName, form.title)
				page.templateRef = form.templateRef
			} else {
				page = null
			}
			if (page != null) {
				pageService.save(page)
				redirect("/")
			} else {
				"oh dear"
			}
		}

		get("page/edit/:refName") {
			val refName = request.params(":refName")
			val page = pageService.get(refName)
			if (page != null) {
				model.put("__page", page)
				engine.render(ModelAndView(model, "page/edit"))
			} else {
				"edit page "
			}
		}
	}
}