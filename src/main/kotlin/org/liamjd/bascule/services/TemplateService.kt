package org.liamjd.bascule.services

import org.liamjd.web.model.PageTemplate

interface TemplateService {

	fun getPageTemplate(refName: String) : PageTemplate?

	fun create(template: PageTemplate) : Long
	fun list(count: Int): List<PageTemplate>
}