package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.model.InputField
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.bascule.services.InputFieldService
import org.liamjd.caisson.extensions.bind
import org.liamjd.web.model.PageTemplate
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post

class BackingForm() {
	var refName: String = ""
	var source: String = ""
	var fields = mutableListOf<InputField>()
}

class NewPageTemplateForm(val refName: String, val source: String, fields: List<NewInputFieldForm>)
class NewInputFieldForm(val refName: String, val fieldType: String)

@SparkController
class PageTemplateController : AbstractController(path = "/pageTemplate") {

	val templateService = DBTemplateService()
	val fieldService = InputFieldService()

	init {

		get("$path/new") {
			engine.render(ModelAndView(model, "pageTemplate/new"))
		}

		post("$path/new") {
			val form = request.bind<PageTemplate>()

			if (form != null) {
				val templateId = templateService.create(form)
				redirect("/pageTemplate/edit/${form.refName}")
			}
		}

		get("$path/edit/:templateRef") {
			val fieldTypes = fieldService.getFieldTypes()
			model.put("__fieldTypes", fieldTypes)
			val pageTemplate = templateService.getPageTemplate(request.params(":templateRef"))
			if (pageTemplate != null) {
				logger.info("Editing template ${pageTemplate}")
				model.put("pageTemplate", pageTemplate)
				request.session().attribute("pageTemplate", pageTemplate)
			} else {
				redirect("/", 404)
			}
			engine.render(ModelAndView(model, "pageTemplate/edit"))
		}

		post("$path/edit/addField") {
			val fieldForm = request.bind<NewInputFieldForm>()
			if (fieldForm != null) {
				/*	val fieldType = templateService.getFieldType(fieldForm.fieldType)
					val newField = InputField(fieldForm.refName, fieldType)
					// do I store the pageTemplate in session? yes...
					val pageTemplate = request.session().attribute<PageTemplate>("pageTemplate")
					pageTemplate.fields.add(newField)
					model.put("pageTemplate",pageTemplate)*/
			}

			engine.render(ModelAndView(model, "pageTemplate/edit"))
		}
	}
}

