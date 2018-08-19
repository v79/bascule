package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.dbConnections
import org.liamjd.bascule.db.entities.*

// classes are used for .new{}, .find{}

class Pages(id: EntityID<Long>) : LongEntity(id) {

	var refName by PAGE.refName
	var uuid by PAGE.uuid
	var createdOn by PAGE.createdOn
	var lastUpdated by PAGE.lastUpdated

	val blocks by Blocks referrersOn BLOCK.page
	val blockGroups by BlockGroups referrersOn BLOCK_GROUP.page

	var template by PageTemplates referencedOn PAGE.template

	var title by PAGE.title
	var dirty by PAGE.dirty

	companion object : LongEntityClass<Pages>(PAGE) {

		fun get(refName: String): Pages? =
				transaction {
					Pages.find { PAGE.refName eq refName }.firstOrNull()
				}

		fun getTemplateName(pageRefName: String): String {
			var templateName: String = ""
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				val page = Pages.find { PAGE.refName eq pageRefName }.firstOrNull()
				if (page != null) {
					val template = PAGE_TEMPLATE.innerJoin(PAGE).slice(PAGE_TEMPLATE.refName)
							.select {
								PAGE_TEMPLATE.id eq PAGE.template and (PAGE.refName eq pageRefName)
							}.distinct().firstOrNull()
					if (template != null) {
						templateName = template[PAGE_TEMPLATE.refName]
					}
				}
			}
			return templateName
		}

		fun new(refName: String, title: String, pageTemplateName: String): Pages {
			val newPage: Pages
			newPage = transaction {
				addLogger(StdOutSqlLogger)
				val template = PAGE_TEMPLATE.slice(PAGE_TEMPLATE.id)
						.select { PAGE_TEMPLATE.refName eq pageTemplateName }
						.first()
				Pages.new {
					this.refName = refName
					this.title = title
					this.dirty = true
					this.template = PageTemplates.wrapRow(template)
				}
			}

			return newPage
		}

		fun update(refName: String, title: String): Boolean {
			val updated = transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)

				PAGE.update({ PAGE.refName eq refName }) {
					it[PAGE.title] = title
				}

			}
			return updated > 0
		}

		fun getBlockGroups(page: Pages): Set<BlockGroups> {
			val groupSet = mutableSetOf<BlockGroups>()
			transaction {
				//			addLogger(StdOutSqlLogger)
				val groups =
						BLOCK_GROUP.innerJoin(PAGE)
								.slice(BLOCK_GROUP.columns)
								.select { BLOCK_GROUP.page eq page.id }

				if (groups.count() != 0) {
					groups.forEach {
						groupSet.add(BlockGroups.wrapRow(it))
					}
				}
			}
			return groupSet
		}

		fun getBlocks(page: Pages): Set<Blocks> {
			val blockSet = mutableSetOf<Blocks>()
			transaction {
				//			addLogger(StdOutSqlLogger)
				val blocks = BLOCK.innerJoin(PAGE)
						.slice(BLOCK.columns)
						.select { BLOCK.page eq page.id and BLOCK.group.isNull() }
				if (blocks.count() != 0) {
					blocks.forEach {
						blockSet.add(Blocks.wrapRow(it))
					}
				}
			}
			return blockSet
		}

		fun getBlockType(blocks: Blocks): BlockTypes? {
			var types: BlockTypes? = null
			transaction {
				//			addLogger(StdOutSqlLogger)
				val result = BLOCK_TYPE.innerJoin(BLOCK)
						.slice(BLOCK_TYPE.columns)
						.select { BLOCK_TYPE.id eq blocks.type.id }
						.firstOrNull()
				if (result != null) {
					types = BlockTypes.wrapRow(result)
				}
			}
			return types
		}

		fun count(): Int = transaction { Pages.count() }

		fun referenceExists(refName: String): Boolean {
			val page = transaction {
				Pages.find { PAGE.refName eq refName }.firstOrNull()
			}
			if (page != null) {
				return true
			}
			return false
		}

		fun list(count: Int): List<Pages> {
			val result = mutableListOf<Pages>()
			transaction(dbConnections.connect()) {
				addLogger(StdOutSqlLogger)
				val all = PAGE.selectAll().limit(count)
				result.addAll(Pages.wrapRows(all).toMutableList())
			}
			return result
		}
	}


}