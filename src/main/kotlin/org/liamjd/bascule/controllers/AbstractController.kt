package org.liamjd.bascule.controllers

import org.slf4j.LoggerFactory
import spark.Request
import spark.Session
import spark.kotlin.after
import spark.kotlin.before
import spark.kotlin.notFound
import spark.template.handlebars.HandlebarsTemplateEngine

/**
 * Turn a simple map of strings into a JSON format string
 */
typealias ErrorMap = HashMap<String,String>
fun ErrorMap.toJson(): String {
	var sb = StringBuilder()
	sb.append("{")
	sb.append("\"errors\": [")
	val size = this.keys.size
	var count = 0
	this.forEach {
		count++
		sb.append("{")
		sb.append("\"field\": \"${it.key}\", ")
		sb.append("\"message\": \"${it.value}\"")
		sb.append("}")

		if (count != size) {
			sb.append(",")
		}
	}
	sb.append("]}")

	return sb.toString()
}

enum class Mode {
	VIEW,
	EDIT,
	PREVIEW
}

abstract class AbstractController(path: String) {
	open val logger = LoggerFactory.getLogger(AbstractController::class.java)

	open var path: String = path

//	protected val engine: ThymeleafTemplateEngine = ThymeleafTemplateEngine()
	protected val engine: HandlebarsTemplateEngine = HandlebarsTemplateEngine("/templates",".hbt")
	val model: MutableMap<String, Any> = hashMapOf<String, Any>()

//	open val authService = FakeAuthService()

	init {
		before {
			model.clear()
			model.put("__mode", Mode.VIEW) // default to viewing
			model.put("__title","Bascule CMS")
			debugSession(session())

			if(!session().attribute<String>("user").isNullOrBlank()) {
				model.put("__user",session().attribute("user"))
			}
		}

		after {

		}

		notFound { "404 not found?"}

	}

	fun debugParams(request: Request) {
		request.params().forEach {
			logger.debug("Param ${it.key} -> ${it.value}")
		}
	}

	fun debugSession(session: Session) {
		logger.info("Session id ${session.id()}")
		logger.info("Session created at ${session.creationTime()}")
		session.attributes().forEach {
			logger.info("Session attr ${it}")
		}
	}
}
