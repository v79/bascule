package org.liamjd.web.model

import java.util.*

data class BlockGroup(val refName: String) {
	val uuid: UUID = UUID.randomUUID()
	val blocks = mutableListOf<Block>()
}