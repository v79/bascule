package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
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
			transaction {
				addLogger(StdOutSqlLogger)

				val pt = wrapRow(PAGE_TEMPLATE.select { PAGE_TEMPLATE.refName eq refName }.first())
				template = pt

			}
			return template
		}

		fun getInputFieldsForTemplate(refName: String): List<InputFields> {
			var fields = listOf<InputFields>()
			transaction {
				addLogger(StdOutSqlLogger)
				val fieldResult = InputFields.wrapRows(INPUT_FIELD.innerJoin(PAGE_TEMPLATE)
						.slice(INPUT_FIELD.columns)
						.select { INPUT_FIELD.pageTemplate eq PAGE_TEMPLATE.id and (PAGE_TEMPLATE.refName eq refName) })
				fields = fieldResult.toList()
			}
			return fields
		}

		fun createPageTemplate(refName: String, source: String): Long {
			val id = transaction {
				addLogger(StdOutSqlLogger)
				PageTemplates.new {
					this.refName = refName
					this.sourceText = source
				}.id.value
			}
			return id
		}

		fun save(refName: String, source: String): Int {
			val result = transaction {
				addLogger(StdOutSqlLogger)
				PAGE_TEMPLATE.update({ PAGE_TEMPLATE.refName eq refName }) {
					it[sourceText] = source
				}
			}
			return result
		}

		fun list(count: Int): List<PageTemplates> {
			val result = mutableListOf<PageTemplates>()
			transaction {
				addLogger(StdOutSqlLogger)
				val all = PAGE_TEMPLATE.selectAll().limit(count)
				result.addAll(PageTemplates.wrapRows(all).toMutableList())
			}
			return result
		}

		fun createInputField(templateRef: String, refName: String, typeRef: String): Int {
			var position = 0;
			transaction {
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

		fun deleteInputField(fieldRef: String): Boolean {
			var fieldCount = 0;
			val result = transaction {
				addLogger(StdOutSqlLogger)
				// what on earth do I do with all the positions
				val row = INPUT_FIELD.slice(INPUT_FIELD.id, INPUT_FIELD.position, INPUT_FIELD.pageTemplate).select { INPUT_FIELD.refName eq fieldRef }.first()
				val fieldToDelete = row[INPUT_FIELD.id]
				val positionToDelete = row[INPUT_FIELD.position]
				val pageTemplate = row[INPUT_FIELD.pageTemplate]
				INPUT_FIELD.deleteWhere { INPUT_FIELD.id eq fieldToDelete }

				// I'm fed up fighting Exposed's chronic lack of documentation

				true
			}
			return result
		}

	}
}