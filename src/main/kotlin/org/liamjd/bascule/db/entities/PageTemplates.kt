package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.dbConnections
import org.liamjd.bascule.db.entities.INPUT_FIELD
import org.liamjd.bascule.db.entities.InputFields
import org.liamjd.bascule.db.entities.PAGE_TEMPLATE
import org.liamjd.bascule.db.entities.RefFieldTypes

class PageTemplates(id: EntityID<Long>) : LongEntity(id) {

	var refName by PAGE_TEMPLATE.refName
	var uuid by PAGE_TEMPLATE.uuid
	var createdOn by PAGE_TEMPLATE.createdOn
	var lastUpdated by PAGE_TEMPLATE.lastUpdated

	val fields by InputFields optionalReferrersOn INPUT_FIELD.pageTemplate
	var sourceText by PAGE_TEMPLATE.sourceText


	companion object : LongEntityClass<PageTemplates>(PAGE_TEMPLATE) {

		fun get(refName: String): PageTemplates? {
			var template: PageTemplates? = null
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)

				val pt = wrapRow(PAGE_TEMPLATE.select { PAGE_TEMPLATE.refName eq refName }.first())
				template = pt

			}
			return template
		}

		fun getInputFieldsForTemplate(refName: String): List<InputFields> {
			var fields = listOf<InputFields>()
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				val fieldResult = InputFields.wrapRows(INPUT_FIELD.innerJoin(PAGE_TEMPLATE)
						.slice(INPUT_FIELD.columns)
						.select { INPUT_FIELD.pageTemplate eq PAGE_TEMPLATE.id and (PAGE_TEMPLATE.refName eq refName) })
				fields = fieldResult.toList()
			}
			return fields
		}

		fun createPageTemplate(refName: String, source: String): Long {
			val id = transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				PageTemplates.new {
					this.refName = refName
					this.sourceText = source
				}.id.value
			}
			return id
		}

		fun save(refName: String, source: String): Int {
			val result = transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				PAGE_TEMPLATE.update({ PAGE_TEMPLATE.refName eq refName }) {
					it[sourceText] = source
				}
			}
			return result
		}

		fun list(count: Int): List<PageTemplates> {
			val result = mutableListOf<PageTemplates>()
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				val all = PAGE_TEMPLATE.selectAll().limit(count)
				result.addAll(PageTemplates.wrapRows(all).toMutableList())
			}
			return result
		}

		fun createInputField(templateRef: String, refName: String, typeRef: String): Int {
			var position = 0;
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				val pT = wrapRow(PAGE_TEMPLATE.select { PAGE_TEMPLATE.refName eq templateRef }.first())
				val fieldCount = INPUT_FIELD.innerJoin(PAGE_TEMPLATE)
						.slice(INPUT_FIELD.columns)
						.select { INPUT_FIELD.pageTemplate eq PAGE_TEMPLATE.id and (PAGE_TEMPLATE.refName eq templateRef) }.count()
				val type = RefFieldTypes.get(typeRef)
				val newField = InputFields.new {
					this.refName = refName
					this.type = type
					this.pageTemplate = pT
					this.position = fieldCount + 1
				}
				position = newField.position
			}

			return position
		}

	}
}