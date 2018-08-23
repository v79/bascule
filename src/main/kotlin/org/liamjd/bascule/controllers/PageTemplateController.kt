package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import org.liamjd.bascule.model.InputField
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.bascule.services.InputFieldService
import org.liamjd.caisson.extensions.bind
import org.liamjd.web.model.PageTemplate
import spark.ModelAndView
import spark.kotlin.before
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
class PageTemplateController : AbstractController() {

	val templateService = DBTemplateService()
	val fieldService = InputFieldService()

	init {

		before("/pageTemplate/*") {
			println("Before PageTemplateController ${request.pathInfo()}")
		}

		get("/pageTemplate/new") {
			engine.render(ModelAndView(model, "pageTemplate/new"))
		}

		post("/pageTemplate/new") {
			val form = request.bind<PageTemplate>()
			if (form != null) {
				val pageTemplate = PageTemplate(form.refName, "")
				val templateId = templateService.create(pageTemplate)
				redirect("/pageTemplate/edit/${pageTemplate.refName}")
			}
		}

		get("/pageTemplate/edit/:templateRef") {
			val fieldTypes = fieldService.getFieldTypes()
			model.put("__fieldTypes", fieldTypes)
			val pageTemplate = templateService.getPageTemplate(request.params(":templateRef"))
			if (pageTemplate != null) {
				// look for new fields stored in session
				val sessionTemplate = request.session().attribute<PageTemplate>("pageTemplate")
				if (sessionTemplate != null) {
					pageTemplate.fields.addAll(sessionTemplate.fields)
				}
				logger.info("Editing template ${pageTemplate}")
				model.put("pageTemplate", pageTemplate)
				request.session().attribute("pageTemplate", pageTemplate)
			} else {
				redirect("/", 404)
			}
			engine.render(ModelAndView(model, "pageTemplate/edit"))
		}

		post("/pageTemplate/edit/addField") {
			val fieldForm = request.bind<NewInputFieldForm>()
			if (fieldForm != null) {
				val fieldType = templateService.getFieldType(fieldForm.fieldType)
					val newField = InputField(fieldForm.refName, fieldType)
					// do I store the pageTemplate in session? yes...
					val pageTemplate = request.session().attribute<PageTemplate>("pageTemplate")
					pageTemplate.fields.add(newField)
				model.put("pageTemplate", pageTemplate)

				pageTemplate.fields
			}

		}

		post("/pageTemplate/edit/save") {
			val form = request.bind<PageTemplate>()
			if (form != null) {
				val sessionTemplate = request.session().attribute<PageTemplate>("pageTemplate")
				if (sessionTemplate.refName == form.refName) {
					templateService.save(form)
					redirect("/pageTemplate/edit/${form.refName}")
				} else {
					"tried to save a different template from the one in session!"
				}
			} else {
				"error"
			}
		}
	}
}

