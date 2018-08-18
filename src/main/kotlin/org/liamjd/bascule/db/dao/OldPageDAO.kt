package org.liamjd.bascule.db.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.AbstractDao
import org.liamjd.bascule.db.Dao
import org.liamjd.bascule.db.entities.*
import org.liamjd.web.db.entities.*

class OldPageDAO : AbstractDao(), Dao {

	fun getPageX(refName: String): Pages? =
			transaction {
				Pages.find { PAGE.refName eq refName }.firstOrNull()
			}

	fun getTemplate(found: Pages): PageTemplates =
			transaction {
				PageTemplates.get(found.template.id)
			}

	fun getBlockGroupsX(page: Pages): Set<BlockGroups> {
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

	fun getBlocksX(page: Pages): Set<Blocks> {
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

	fun getBlockTypeX(blocks: Blocks): BlockTypes? {
		var types: BlockTypes? = null
		transaction {
//			addLogger(StdOutSqlLogger)
			val result = BLOCK_TYPE.innerJoin(BLOCK)
					.slice(BLOCK_TYPE.columns)
					.select { BLOCK_TYPE.id eq blocks.type.id }
					.firstOrNull()
			if(result != null) {
				types = BlockTypes.wrapRow(result)
			}
		}
		return types
	}

	fun countPagesX(): Int = transaction { Pages.count() }

	fun getAllTemplates() : List<PageTemplates> {
		var list = listOf<PageTemplates>()
		transaction {
			val result = PAGE_TEMPLATE.selectAll()
			list = PageTemplates.wrapRows(result).toList()
		}

		return list
	}

	fun save(page: Pages) : Pages {
		TODO("not implemented")
	}

	fun createPage(refName: String, title: String, pageTemplateName: String): Pages {
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
}