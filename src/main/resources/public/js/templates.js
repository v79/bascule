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
			console.log("xhr -> " + xhr);
			$('#' + 'page_template_field_list').html(response);
		}

	});
}

function newTemplateDeleteField(fieldReference) {
	$.ajax({
		url: '/pageTemplate/edit/deleteField',
		method: 'post',
		data: fieldReference,
		success: function (response, statusText, xhr) {
			$('#' + 'page_template_field_list').html(response);
		}
	});
}
