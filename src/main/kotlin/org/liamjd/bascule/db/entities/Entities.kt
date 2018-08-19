package org.liamjd.bascule.db.entities

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.joda.time.DateTime
import java.time.Instant
import java.util.*

// objects are tables(schemas), used for joins, to refer to columns - maybe in CAPS?
// classes are used for .new{}, .find{}

// common fields
// val refName = varchar("ref_name",255).uniqueIndex()
//	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
// val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
// last_updated
// user?

/**
 * Exposed is using joda.time but I want to use Java8 times. Store all dates as UTC
 */
fun Instant.toDateTime(): DateTime {
	return DateTime(this.toEpochMilli())
}

object BLOCK_TYPE : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val description = varchar("description",1023)
}

object BLOCK : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val page = reference("page",PAGE,onDelete = ReferenceOption.CASCADE)
	val group = optReference("group",BLOCK_GROUP,onDelete = ReferenceOption.CASCADE)
	val template = reference("template",BLOCK_TEMPLATE)
	val type = reference("type",BLOCK_TYPE)

	val content = text("content")
}

object PAGE : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val template = reference("template",PAGE_TEMPLATE)

	val title = varchar("title", 1023).index()
	val dirty = bool("dirty")
}

object PAGE_TEMPLATE : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val sourceText = text("source")

}

object BLOCK_GROUP : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val page = reference("page",PAGE,onDelete = ReferenceOption.CASCADE)

}

object BLOCK_TEMPLATE : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val sourceText = text("source")

}

object INPUT_FIELD : LongIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	val uuid = uuid("uuid").clientDefault { UUID.randomUUID() }
	val createdOn = datetime("created_on").clientDefault { Instant.now().toDateTime() }
	val lastUpdated = datetime("updated_on").clientDefault { Instant.now().toDateTime() }

	val description = varchar("description", 1023).nullable()
	val type = reference("type", REF_FIELD_TYPE)
	val position = integer("pos")

	val pageTemplate = optReference("page_template", PAGE_TEMPLATE)
	val blockTemplate = optReference("block_template", BLOCK_TEMPLATE)

	// validation? rules?

}


object REF_FIELD_TYPE : IntIdTable() {
	val refName = varchar("ref_name", 255).uniqueIndex()
	// validation? rules? a class?

}