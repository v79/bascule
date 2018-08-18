package org.liamjd.web.db.entities

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.liamjd.bascule.db.entities.BLOCK

class Blocks(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<Blocks>(BLOCK)

	var refName by BLOCK.refName
	var uuid by BLOCK.uuid
	var createdOn by BLOCK.createdOn
	var lastUpdated by BLOCK.lastUpdated

	var page by Pages referencedOn BLOCK.page
	var group by BlockGroups optionalReferencedOn BLOCK.group
	var template by BlockTemplates referencedOn BLOCK.template
	var type by BlockTypes referencedOn BLOCK.type

	var content by BLOCK.content
}