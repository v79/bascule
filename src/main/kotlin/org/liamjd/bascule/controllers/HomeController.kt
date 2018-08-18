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
				redirect("${path}page/new/${form.refName}")
			}
		}

		// pages
		get("${path}page/new/:template") {
			val template = templateService.getPageTemplate(request.params("template"))

			if(template != null) {
				model.put("__title", "New page")
				model.put("__pageTemplate", template)
				engine.render(ModelAndView(model, "page/new"))
			} else {
				"page template not found"
			}
		}

		post("${path}page/new/new") {
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
				"Now, save page ${page}"
			} else {
				"oh dear"
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