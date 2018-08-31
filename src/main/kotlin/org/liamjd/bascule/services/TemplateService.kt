package org.liamjd.bascule.services

import org.liamjd.bascule.model.InputField
import org.liamjd.bascule.model.InputFieldType
import org.liamjd.web.model.PageTemplate

interface TemplateService {

	fun getPageTemplate(refName: String) : PageTemplate?

	fun create(template: PageTemplate) : Long
	fun listPageTemplates(count: Int): List<PageTemplate>
	fun save(template: PageTemplate): Int
	fun getFieldType(refName: String): InputFieldType
	fun addInputField(templateRef: String, newField: InputField): Int
	fun deleteInputField(fieldRef: String): Boolean
}