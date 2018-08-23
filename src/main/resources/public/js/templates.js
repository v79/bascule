function newTemplateAddField() {

	var serializedData = $('#' + pageTemplate_fields).serialize();
	console.log(serializedData);
	$.ajax({
		url: 'pageTemplate/addField',
		method: 'post',
		data: serializedData,
		success: function (response, statusText, xhr) {
			// if in error, form should be re-rendered
			$('#page_template_field_list').html(response);
		}

	});
}