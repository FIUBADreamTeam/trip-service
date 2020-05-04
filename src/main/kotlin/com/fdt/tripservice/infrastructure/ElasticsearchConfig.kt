package com.fdt.tripservice.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.client.Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.DefaultResultMapper
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.stereotype.Component

@Configuration
class ElasticsearchConfig(val elasticsearchEntityMapper: ElasticsearchEntityMapper) {
    @Bean
    fun elasticsearchTemplate(client: Client) =
            ElasticsearchTemplate(client, DefaultResultMapper(elasticsearchEntityMapper))
}

@Component
class ElasticsearchEntityMapper(val objectMapper: ObjectMapper) : EntityMapper {
    override fun mapToString(`object`: Any?) = objectMapper.writeValueAsString(`object`)

    override fun <T : Any?> mapToObject(source: String?, clazz: Class<T>?) = objectMapper.readValue(source, clazz)

    override fun <T : Any?> readObject(source: MutableMap<String, Any>?, targetType: Class<T>?): T? {
        TODO("Not yet implemented")
    }

    override fun mapObject(source: Any?): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

}
