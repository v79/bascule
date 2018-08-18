package org.liamjd.bascule.services

import org.liamjd.bascule.db.dao.PageDAO
import org.liamjd.web.db.entities.Pages
import org.liamjd.web.model.Page
import org.liamjd.web.model.PageTemplate

class DBPageService : PageService {

	val pageDao = PageDAO()

	override fun getPage(refName: String): Page? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun save(page: Page): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun countPages(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getPageTemplates(): List<PageTemplate> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isUniqueRef(refName: String): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createPage(refName: String, title: String, pageTemplateName: String): Page {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}


}