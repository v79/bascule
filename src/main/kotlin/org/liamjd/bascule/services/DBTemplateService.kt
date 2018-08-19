package org.liamjd.bascule.services

import org.liamjd.web.db.entities.PageTemplates
import org.liamjd.web.model.PageTemplate

class DBTemplateService : TemplateService {

	override fun create(template: PageTemplate): Long {

		val result = PageTemplates.createPageTemplate(template.refName, template.source)
		return result
	}

	override fun getPageTemplate(refName: String): PageTemplate? {

		val result = PageTemplates.get(refName)

		if(result != null) {
			return PageTemplate(result.refName,result.sourceText)
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
}