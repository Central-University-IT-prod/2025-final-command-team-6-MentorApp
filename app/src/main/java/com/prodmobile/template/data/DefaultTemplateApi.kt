package com.prodmobile.template.data

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol

class DefaultTemplateApi: TemplateApi {
    override val url
        get() = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "prod-team-6-a36eo8k0.REDACTED",
            pathSegments = listOf()
        )
}

interface TemplateApi {
    val url: URLBuilder
}