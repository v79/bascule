package org.liamjd.bascule.render

import com.github.jknack.handlebars.Context
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.context.FieldValueResolver
import com.github.jknack.handlebars.context.JavaBeanValueResolver
import com.github.jknack.handlebars.context.MapValueResolver
import com.github.jknack.handlebars.context.MethodValueResolver
import org.liamjd.bascule.services.DBPageService
import org.liamjd.bascule.services.DBTemplateService
import org.liamjd.web.model.Page

class HandlebarsRenderService : RenderService {

	val pageService = DBPageService()
	val templateService = DBTemplateService()

	fun render(page: Page, mode: RenderMode): String {

		val renderer = Handlebars()

		val model = buildModelMap(page)
		val template = templateService.getPageTemplate(page.templateRef)


		val hbContext = Context.newBuilder(model)
				.resolver(MethodValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE, MapValueResolver.INSTANCE, FieldValueResolver.INSTANCE)
				.build()

		if (template != null) {
			val hbTemplate = renderer.compileInline(template.source)
			return hbTemplate.apply(hbContext)
		}

		return "error building page because couldn't find template ${page.templateRef}"
	}

	private fun buildModelMap(page: Page): Map<String, Any> {
		val map = mutableMapOf<String, Any>()
		map.put("title", page.title)
		map.put("blocks", page.blocks)
		// blocks, blockgroups, input fields...

		return map
	}
}