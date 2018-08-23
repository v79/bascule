package org.liamjd.bascule.controllers

import org.liamjd.bascule.annotations.SparkController
import spark.kotlin.before
import spark.kotlin.get

@SparkController
class WobbleController : AbstractController(path = "/wobble") {
	init {
		before("/wobble") {
			logger.info("before(/wobble) called in WobbleController")
		}
		get(path) {
			"wobbled"
		}
	}
}