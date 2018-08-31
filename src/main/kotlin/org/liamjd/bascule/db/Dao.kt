package org.liamjd.bascule.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.entities.*
import org.slf4j.LoggerFactory

interface Dao

object dbConnections {
	val connectionString = "jdbc:mysql://127.0.0.1:3306/"
	val driverClass = "org.mariadb.jdbc.Driver"
	val dbDatabase = "bascule"
	val dbPassword = "indy25tlx"
	val dbUser = "liam"

	val daoLogger = LoggerFactory.getLogger(this::class.java)

	init {
		daoLogger.info("Connecting to DB: ${connectionString}${dbDatabase}")  // ?user=${dbUser}&password=${dbPassword}
		daoLogger.info("Using driver: ${driverClass}")
	}

	fun connect() = Database.connect(url = connectionString + dbDatabase, user = dbUser, password = dbPassword, driver = driverClass)

	fun initialize() {
		transaction(connect()) {
			addLogger(StdOutSqlLogger)
			SchemaUtils.drop(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE, INPUT_FIELD, REF_FIELD_TYPE)
			SchemaUtils.create(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE, INPUT_FIELD, REF_FIELD_TYPE)
			//TODO: do something clever with reflection?
//			SchemaUtils.createMissingTablesAndColumns(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE)


		}

		addReferenceData()
	}

	private fun addReferenceData() {
		transaction(connect()) {
			addLogger(StdOutSqlLogger)
			RefFieldTypes.new {
				this.refName = "Text"
			}
			RefFieldTypes.new {
				this.refName = "Number"
			}
			RefFieldTypes.new {
				this.refName = "DateTime"
			}
			RefFieldTypes.new {
				this.refName = "MediaReference"
			}
			RefFieldTypes.new {
				this.refName = "RichText"
			}
		}
	}
}