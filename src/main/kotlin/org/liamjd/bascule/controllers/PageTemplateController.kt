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

@SparkController
class PageTemplateController : AbstractController(path = "/pageTemplate") {

	val templateService = DBTemplateService()
	val fieldService = InputFieldService()

	init {
		get("$path/new") {
			val fields = mutableListOf<InputField>()
			val newestField = getFromFlash(request.session(), "flash_newField")
			if (newestField != null) {
				fields.add(newestField as InputField)
			}
			val fieldTypes = fieldService.getFieldTypes()

			model.put("__fields", fields)
			model.put("__fieldTypes", fieldTypes)
			engine.render(ModelAndView(model, "pageTemplate/new"))
		}

		post("$path/new") {
			val form = request.bind<PageTemplate>()

			if (form != null) {
				templateService.create(form)
				redirect("/")
			}
		}

		post("$path/addField") {
			val newFieldForm = request.bind<NewInputFieldForm>()

			if (newFieldForm != null) {
				val type = fieldService.getFieldType(newFieldForm.fieldType)
				val field = InputField(newFieldForm.refName, type)

				// now I really need a flash...
				flash(request.session(), "flash_newField", field)
			}


			redirect("$path/new")
		}
	}
}

class NewInputFieldForm(val refName: String, val fieldType: String)