package com.justai.jaicf.template.serializers

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.KotlinModule

object Jackson {
    val mapper = ObjectMapper()

    init {
        mapper.registerModule(KotlinModule())
        mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        mapper.enable(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    }


    fun parse(json: String) = mapper.readTree(json)

    inline fun <reified T> parse(json: String): T =
        mapper.readValue(json, T::class.java)

    fun <T> parse(json: String, clazz: Class<T>): T =
        mapper.readValue(json, clazz)

    fun <T> parseNullable(json: String?, clazz: Class<T>): T? {
        json ?: return null
        return mapper.readValue(json, clazz)
    }

    fun <T> parse(json: String, tr: TypeReference<T>): T =
        mapper.readValue(json, tr)

    inline fun <reified T> parse(json: JsonNode): T =
        mapper.treeToValue(json, T::class.java)

    fun <T> parse(json: JsonNode, clazz: Class<T>): T =
        mapper.treeToValue(json, clazz)

    fun <T> stringify(data: T): String =
        mapper.writeValueAsString(data)

    fun <T> stringifyNullable(data: T?): String? {
        data ?: return null
        return mapper.writeValueAsString(data)
    }

    fun toNode(data: Any): JsonNode =
        mapper.valueToTree(data)

    fun toObject(data: Any): ObjectNode =
        mapper.valueToTree(data)

    fun objectNode() = mapper.createObjectNode()

    private fun mergeArrayNodes(oldValue: ArrayNode, newValue: ArrayNode): ArrayNode {
        val finalArray = oldValue.toList() + newValue.toList()
        return mapper.createArrayNode().addAll(finalArray.distinct())
    }

    fun merge(oldValue: JsonNode, newValue: JsonNode): JsonNode {
        return when {
            oldValue.isArray && newValue.isArray -> mergeArrayNodes(oldValue as ArrayNode, newValue as ArrayNode)
            oldValue.isObject && newValue.isObject -> mergeObjectNodes(oldValue as ObjectNode, newValue as ObjectNode)
            else -> throw RuntimeException("Method for node types not specified")
        }
    }

    private fun mergeObjectNodes(oldValue: ObjectNode, newValue: ObjectNode): ObjectNode {
        val result: ObjectNode = objectNode()
        val elements = oldValue.fields()
        for ((field, value) in elements) {
            if (value.fields().hasNext()) {
                result.set<JsonNode>(field, mergeObjectNodes(field, value, newValue))
            } else {
                result.set<JsonNode>(field, mergeFields(field, value, newValue))
            }
        }
        return appendNewElements(result, newValue)
    }

    private fun appendNewElements(result: ObjectNode, newValue: JsonNode): ObjectNode {
        val newElements = newValue.fields()
        for ((field, value) in newElements) {
            if (result[field] == null) result.set<JsonNode>(field, value)
        }
        return result
    }

    private fun mergeObjectNodes(field: String, value: JsonNode, newValue: JsonNode): JsonNode {
        return if (newValue[field] != null) {
            merge(toObject(value), toObject(newValue[field]))
        } else {
            value
        }
    }

    private fun mergeFields(field: String, value: JsonNode, newValue: JsonNode): JsonNode {
        return if (newValue[field] != null) {
            newValue[field]
        } else {
            value
        }
    }
}
