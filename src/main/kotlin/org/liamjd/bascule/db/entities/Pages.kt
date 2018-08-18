package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.entities.BLOCK
import org.liamjd.bascule.db.entities.BLOCK_GROUP
import org.liamjd.bascule.db.entities.PAGE

// classes are used for .new{}, .find{}

class Pages(id: EntityID<Long>) : LongEntity(id) {

	companion object : LongEntityClass<Pages>(PAGE)

	var refName by PAGE.refName
	var uuid by PAGE.uuid
	var createdOn by PAGE.createdOn
	var lastUpdated by PAGE.lastUpdated

	val blocks by Blocks referrersOn BLOCK.page
	val blockGroups by BlockGroups referrersOn BLOCK_GROUP.page

	var template by PageTemplates referencedOn  PAGE.template

	var title by PAGE.title
	var dirty by PAGE.dirty

	fun get(refName: String): Pages? =
		transaction {
			Pages.find { PAGE.refName eq refName }.firstOrNull()
	}

}