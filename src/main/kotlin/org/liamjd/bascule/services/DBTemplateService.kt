package org.liamjd.bascule.services

import org.liamjd.bascule.db.entities.RefFieldTypes
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

		if(result != null) {
			val pageTemplate = PageTemplate(result.refName, result.sourceText)

			// TODO: why do I have to do all this? What's an ORM form if not to help here...
			val inputFields = PageTemplates.getInputFieldsForTemplate(refName)
			if (inputFields.isNotEmpty()) {
				// this still doesn't work - I need this to be much more eager!
				/*inputFields.forEach {
					val type = InputFieldType(RefFieldTypes.get(it.type.id.value).refName)
					pageTemplate.fields.add(InputField(it.refName,type ))
				}*/
			}
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
}