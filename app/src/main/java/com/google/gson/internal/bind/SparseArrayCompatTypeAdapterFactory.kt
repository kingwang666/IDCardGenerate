package com.google.gson.internal.bind

import androidx.collection.SparseArrayCompat
import androidx.collection.forEach
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.JsonReaderInternalAccess
import com.google.gson.internal.KWGsonTypes
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
@Suppress("UNCHECKED_CAST")
class SparseArrayCompatTypeAdapterFactory @JvmOverloads constructor(private val complexMapKeySerialization: Boolean = false) :
    TypeAdapterFactory {

    override fun <T : Any> create(gson: Gson, typeToken: TypeToken<T>): TypeAdapter<T>? {
        val type: Type = typeToken.type

        val rawType: Class<in T> = typeToken.rawType
        if (!SparseArrayCompat::class.java.isAssignableFrom(rawType)) {
            return null
        }

        val keyAdapter: TypeAdapter<Int> = gson.getAdapter(Int::class.java)
        val valueType = KWGsonTypes.getSparseArrayCompatType(type, rawType)


        val valueAdapter: TypeAdapter<out Any> = gson.getAdapter(TypeToken.get(valueType))


        return Adapter<Nothing>(
            gson,
            keyAdapter,
            complexMapKeySerialization,
            valueType,
            valueAdapter
        ) as TypeAdapter<T>
    }

    internal class Adapter<V>(
        context: Gson,
        private val keyTypeAdapter: TypeAdapter<Int>,
        private val complexMapKeySerialization: Boolean,
        valueType: Type,
        valueTypeAdapter: TypeAdapter<out Any>
    ) : TypeAdapter<SparseArrayCompat<V>>() {

        private val valueTypeAdapter =
            TypeAdapterRuntimeTypeWrapper(context, valueTypeAdapter as TypeAdapter<V>, valueType)

        @Throws(IOException::class)
        override fun read(reader: JsonReader): SparseArrayCompat<V>? {
            val peek = reader.peek()
            if (peek == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val sparseArray = SparseArrayCompat<V>()
            if (peek == JsonToken.BEGIN_ARRAY) {
                reader.beginArray()
                while (reader.hasNext()) {
                    reader.beginArray() // entry array
                    val key = keyTypeAdapter.read(reader)
                    val value = valueTypeAdapter.read(reader)
                    sparseArray.put(key, value)
                    reader.endArray()
                }
                reader.endArray()
            } else {
                reader.beginObject()
                while (reader.hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(reader)
                    val key = keyTypeAdapter.read(reader)
                    val value = valueTypeAdapter.read(reader)
                    sparseArray.put(key, value)
                }
                reader.endObject()
            }
            return sparseArray
        }

        @Throws(IOException::class)
        override fun write(
            writer: JsonWriter,
            sparseArray: SparseArrayCompat<V>?
        ) {
            if (sparseArray == null) {
                writer.nullValue()
                return
            }
            if (!complexMapKeySerialization) {
                writer.beginObject()
                sparseArray.forEach { key, value ->
                    writer.name(key.toString())
                    valueTypeAdapter.write(writer, value)
                }
                writer.endObject()
                return
            }
            var hasComplexKeys = false
            val keys: MutableList<JsonElement> =
                ArrayList(sparseArray.size())
            val values: MutableList<V> = ArrayList(sparseArray.size())
            sparseArray.forEach { key, value ->
                val keyElement = keyTypeAdapter.toJsonTree(key)
                keys.add(keyElement)
                values.add(value)
                hasComplexKeys =
                    hasComplexKeys or (keyElement.isJsonArray || keyElement.isJsonObject)
            }
            if (hasComplexKeys) {
                writer.beginArray()
                var i = 0
                val size = keys.size
                while (i < size) {
                    writer.beginArray() // entry array
                    Streams.write(keys[i], writer)
                    valueTypeAdapter.write(writer, values[i])
                    writer.endArray()
                    i++
                }
                writer.endArray()
            } else {
                writer.beginObject()
                var i = 0
                val size = keys.size
                while (i < size) {
                    val keyElement = keys[i]
                    writer.name(keyToString(keyElement))
                    valueTypeAdapter.write(writer, values[i])
                    i++
                }
                writer.endObject()
            }
        }

        private fun keyToString(keyElement: JsonElement): String {
            return if (keyElement.isJsonPrimitive) {
                val primitive = keyElement.asJsonPrimitive
                if (primitive.isNumber) {
                    primitive.asNumber.toString()
                } else if (primitive.isBoolean) {
                    primitive.asBoolean.toString()
                } else if (primitive.isString) {
                    primitive.asString
                } else {
                    throw AssertionError()
                }
            } else if (keyElement.isJsonNull) {
                "null"
            } else {
                throw AssertionError()
            }
        }


    }
}