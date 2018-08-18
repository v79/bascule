package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.liamjd.bascule.db.entities.BLOCK
import org.liamjd.bascule.db.entities.BLOCK_GROUP

class BlockGroups(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<BlockGroups>(BLOCK_GROUP)

	var refName by BLOCK_GROUP.refName
	var uuid by BLOCK_GROUP.uuid
	var createdOn by BLOCK_GROUP.createdOn
	var lastUpdated by BLOCK_GROUP.lastUpdated

	var page by Pages referencedOn BLOCK_GROUP.page
	val blocks by Blocks optionalReferrersOn BLOCK.group
}