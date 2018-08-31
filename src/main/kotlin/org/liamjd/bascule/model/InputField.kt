package org.liamjd.bascule.model

data class InputField(val refName: String, val type: InputFieldType) {
	var description: String = ""
	var position: Int = 0

}

data class InputFieldType(val refName: String) {

}