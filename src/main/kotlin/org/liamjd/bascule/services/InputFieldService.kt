package org.liamjd.bascule.services

import org.liamjd.bascule.db.entities.RefFieldTypes
import org.liamjd.bascule.model.InputFieldType

class InputFieldService {

	fun getFieldTypes(): Set<InputFieldType> {
		val rows = RefFieldTypes.list()
		val result = mutableSetOf<InputFieldType>()
		rows.forEach {
			result.add(InputFieldType(it.refName))
		}
		return result
	}

	fun getFieldType(refName: String): InputFieldType {
		val row = RefFieldTypes.get(refName)
		return InputFieldType(row.refName)
	}
}