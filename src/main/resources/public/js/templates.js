$(document).ready(function () {

});

function newTemplateAddField() {

	var serializedData = $('#' + 'pageTemplate_fields').serialize();
	console.log(serializedData);
	$.ajax({
		url: '/pageTemplate/edit/addField',
		method: 'post',
		data: serializedData,
		success: function (response, statusText, xhr) {
			console.log("response -> " + response);
			console.log("statusText -> " + statusText);
			console.log("xhr -> " + xhr)
		}

	});
}

