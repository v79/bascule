package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.caisson.extensions.bind
import org.liamjd.web.model.Page
import org.liamjd.web.model.PageTemplate
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post

@SparkController
class HomeController : AbstractController(path = "/") {

	val templateService = DBTemplateService()
	val pageService = DBPageService()

	init {

		get(path) {

			val topPages = pageService.listPages(10)
			model.put("pages", topPages)

			debugModel()
			engine.render(ModelAndView(model,"home"))
		}

		get("$path/other") {
			debugModel()
			engine.render(ModelAndView(model,"other"))
		}

		// page templates
		get("$path/pageTemplate/new") {
			engine.render(ModelAndView(model,"pageTemplate/new"))
		}

		post("$path/pageTemplate/new") {
			val form = request.bind<PageTemplate>()

			if(form!=null) {
				templateService.create(form)
				redirect(path)
			}
		}

		// pages
		get("${path}page/new") {
			val templates = templateService.list(10)

			model.put("__title", "New page")

			if (templates.isNotEmpty()) {
				model.put("__pageTemplates", templates)

				debugModel()

				engine.render(ModelAndView(model, "page/new"))
			} else {
				"no templates!"
			}
		}

		post("${path}page/new/create") {
			val form = request.bind<PageForm>()
			val page: Page?
			if(form != null) {
				page = Page(form.refName, form.title)
				page.templateRef = form.templateRef
			} else {
				page = null
			}
			if(page != null) {
				pageService.save(page)
				redirect(path)
			} else {
				"oh dear"
			}
		}

		get("${path}page/edit/:refName") {
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

	fun debugModel() {
		model.keys.forEach {
			println("$it -> ${model[it]}")
		}
	}
}

class PageForm(val refName: String, val title: String, val templateRef: String)