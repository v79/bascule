package org.liamjd.bascule.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.liamjd.bascule.db.entities.*
import org.slf4j.LoggerFactory

object dbConnections {
	val connectionString = "jdbc:mysql://127.0.0.1:3306/"
	val driverClass = "org.mariadb.jdbc.Driver"
	val dbDatabase = "bascule"
	val dbPassword = "indy25tlx"
	val dbUser = "liam"
}

abstract class AbstractDao : Dao {
	open val daoLogger = LoggerFactory.getLogger(AbstractDao::class.java)

	init {
		val dbConnectionString: String = dbConnections.connectionString
		val dbName: String = dbConnections.dbDatabase
		val dbUser: String = dbConnections.dbUser
		val dbPassword: String = dbConnections.dbPassword
		val dbDriver: String = dbConnections.driverClass

		daoLogger.info("Connecting to DB: ${dbConnectionString}${dbName}")  // ?user=${dbUser}&password=${dbPassword}
		daoLogger.info("Using driver: ${dbDriver}")
		Database.connect(url = dbConnectionString + dbName, user = dbUser, password = dbPassword, driver = dbDriver)

		transaction {
			addLogger(StdOutSqlLogger)
			SchemaUtils.drop(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE)
			SchemaUtils.create(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE)
			//TODO: do something clever with reflection?
//			SchemaUtils.createMissingTablesAndColumns(PAGE, PAGE_TEMPLATE, BLOCK, BLOCK_TEMPLATE, BLOCK_GROUP, BLOCK_TYPE)
		}
	}
}