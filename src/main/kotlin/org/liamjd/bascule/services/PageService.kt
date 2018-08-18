package org.liamjd.bascule.services

import org.liamjd.web.model.Page
import org.liamjd.web.model.PageTemplate

interface PageService {
	fun get(refName: String): Page?

	fun save(page: Page) : Boolean

	fun countPages() : Int

	fun getPageTemplates() : List<PageTemplate>

	fun isUniqueRef(refName: String) : Boolean

	fun createPage(refName: String, title: String, pageTemplateName: String): Page
}