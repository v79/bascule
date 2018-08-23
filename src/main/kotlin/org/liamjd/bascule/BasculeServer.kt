package org.liamjd.bascule

import org.liamjd.bascule.annotations.SparkController
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.slf4j.LoggerFactory
import spark.kotlin.port
import spark.kotlin.staticFiles
import spark.servlet.SparkApplication
import java.time.LocalDate

class BasculeServer : SparkApplication {
	val logger = LoggerFactory.getLogger(this::class.java)
	val thisPackage = this.javaClass.`package`

	constructor(args: Array<String>) {
		val portNumber: String? = System.getProperty("server.port")
		port(number = portNumber?.toInt() ?: 4580)

		staticFiles.location("/public")

		// initialize database
		//dbConnections.initialize()

		val reflections = Reflections(thisPackage.name, MethodAnnotationsScanner(), TypeAnnotationsScanner(), SubTypesScanner())
		val controllers = reflections.getTypesAnnotatedWith(SparkController::class.java)
		controllers.forEach {
			logger.info("Instantiating controller " + it.simpleName)
			it.newInstance()
		}

		displayStartupMessage(portNumber = portNumber?.toInt())
	}


	override fun init() {

	}

	private fun displayStartupMessage(portNumber: Int?) {
		logger.info("=============================================================")
		logger.info("BasculeCMS web server Started")
		logger.info("Date: " + LocalDate.now().toString())
		logger.info("OS: " + System.getProperty("os.refName"))
		logger.info("Port: " + if (portNumber != null) portNumber else "4568")
		logger.info("JDBC URL: " + System.getenv("JDBC_DATABASE_URL"))
		logger.info("=============================================================")
	}
}
