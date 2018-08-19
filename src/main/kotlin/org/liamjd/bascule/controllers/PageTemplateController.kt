package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.caisson.extensions.bind
import org.liamjd.web.model.PageTemplate
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post

@SparkController
class PageTemplateController : AbstractController(path = "/pageTemplate") {

	val templateService = DBTemplateService()

	init {
		get("$path/new") {
			engine.render(ModelAndView(model, "pageTemplate/new"))
		}

		post("$path/new") {
			val form = request.bind<PageTemplate>()

			if (form != null) {
				templateService.create(form)
				redirect("/")
			}
		}
	}
}