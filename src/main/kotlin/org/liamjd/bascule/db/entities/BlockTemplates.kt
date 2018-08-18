package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.liamjd.bascule.db.entities.BLOCK_TEMPLATE

class BlockTemplates(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<BlockTemplates>(BLOCK_TEMPLATE)

	var refName by BLOCK_TEMPLATE.refName
	var uuid by BLOCK_TEMPLATE.uuid
	var createdOn by BLOCK_TEMPLATE.createdOn
	var lastUpdated by BLOCK_TEMPLATE.lastUpdated

	var sourceText by BLOCK_TEMPLATE.sourceText
}