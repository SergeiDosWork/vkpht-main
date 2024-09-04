package me.goodt.nsi

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.Schema

class OpenApiMetaInfoProvider {
    val entityMetadata: OpenAPI
        get() {
            val openApi = OpenAPI()
            val components = Components()
            val productSchema = Schema<Any>()
                    .addProperty("id", Schema<Long>().type("integer").format("int64"))
                    .addProperty("name", Schema<String>().type("string"))
                    .addProperty("description", Schema<String>().type("string"))
            components.addSchemas("Product", productSchema)
            openApi.components(components)
            return openApi
        }
}
