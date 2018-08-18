package org.liamjd.bascule.services

import org.liamjd.web.db.entities.Pages
import org.liamjd.web.model.Page
import org.liamjd.web.model.PageTemplate

class DBPageService : PageService {

	override fun get(refName: String): Page? {
		var page: Page? = null
		val entity = Pages.get(refName)
		if (entity != null) {
			page = Page(entity.refName, entity.title)
		}
		return page
	}

	override fun save(page: Page): Boolean {
		if (Pages.referenceExists(page.refName)) {
			return Pages.update(page.refName, page.title)
		} else {
			if (Pages.new(page.refName, page.title, page.templateRef) != null) {
				return true
			}
		}
		return false
	}

	override fun countPages(): Int {
		return Pages.count()
	}

	override fun getPageTemplates(): List<PageTemplate> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isUniqueRef(refName: String): Boolean {
		return !Pages.referenceExists(refName)
	}

	override fun createPage(refName: String, title: String, pageTemplateName: String): Page {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}