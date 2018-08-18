package org.liamjd.bascule.services

import org.liamjd.bascule.db.dao.PageTemplateDao
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
}