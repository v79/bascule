package org.liamjd.bascule.services

import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.entities.RefFieldTypes
import org.liamjd.bascule.model.InputField
import org.liamjd.bascule.model.InputFieldType
import org.liamjd.web.db.entities.PageTemplates
import org.liamjd.web.model.PageTemplate
import org.slf4j.LoggerFactory

class DBTemplateService : TemplateService {

	val logger = LoggerFactory.getLogger(this.javaClass)

	override fun create(template: PageTemplate): Long {

		val result = PageTemplates.createPageTemplate(template.refName, template.source)
		return result
	}

	override fun getPageTemplate(refName: String): PageTemplate? {

		val result = PageTemplates.get(refName)
		lateinit var pageTemplate: PageTemplate
		if (result != null) {
			transaction {
				pageTemplate = PageTemplate(result.refName, result.sourceText)

				// TODO: why do I have to do all this? What's an ORM for if not to help here...
				val inputFields = PageTemplates.getInputFieldsForTemplate(refName)
				if (inputFields.isNotEmpty()) {
					inputFields.forEach {
						// TODO add model converter function
						val type = InputFieldType(RefFieldTypes.get(it.type.id.value).refName)
						val field = InputField(it.refName, type)
						field.position = it.position
						field.description = it.description ?: ""
						pageTemplate.fields.add(field)
					}
				}
			}
			println(pageTemplate)
			return pageTemplate

		}
		return null
	}

	override fun listPageTemplates(count: Int): List<PageTemplate> {
		val rows = PageTemplates.list(count)
		val result = mutableListOf<PageTemplate>()
		if (rows.isNotEmpty()) {
			rows.forEach {
				val template = PageTemplate(it.refName, it.sourceText)
				result.add(template)
			}
		}
		return result
	}

	override fun save(template: PageTemplate): Int {
		return PageTemplates.save(template.refName, template.source)
	}

	override fun getFieldType(refName: String): InputFieldType {
		val row = RefFieldTypes.get(refName)
		return InputFieldType(row.refName)
	}

	override fun addInputField(templateRef: String, newField: InputField): Int {
		val position = PageTemplates.createInputField(templateRef, newField.refName, newField.type.refName)
		return position
	}

	override fun deleteInputField(fieldRef: String): Boolean {
		return PageTemplates.deleteInputField(fieldRef)
	}
}