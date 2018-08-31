package org.liamjd.bascule.controllers

import org.slf4j.LoggerFactory
import spark.Request
import spark.Session
import spark.template.handlebars.HandlebarsTemplateEngine
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

abstract class AbstractController() {
	open val logger = LoggerFactory.getLogger(AbstractController::class.java)

	protected val engine: HandlebarsTemplateEngine = HandlebarsTemplateEngine("/templates",".hbt")

	val model: MutableMap<String, Any> = hashMapOf<String, Any>()

	fun flash(session: Session, key: String, obj: Any) {
		session.attribute(key, obj)
	}

	fun getFromFlash(session: Session, key: String): Any? {
		val obj = session.attribute<Any?>(key)
		session.removeAttribute(key)
		return obj
	}

	fun debugParams(request: Request) {
		request.params().forEach {
			logger.debug("Param ${it.key} -> ${it.value}")
		}
	}

	fun debugSession(session: Session) {
		val zone: ZoneId = ZoneId.systemDefault()
		val df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS").withZone(zone)
		logger.info("Session id ${session.id()} created at ${df.format(Instant.ofEpochMilli(session.creationTime()))}")
		session.attributes().forEach {
			logger.info("Session attr ${it}")
		}
	}

	fun debugModel() {
		model.keys.forEach {
			println("$it -> ${model[it]}")
		}
	}
}
