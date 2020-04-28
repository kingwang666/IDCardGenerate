package com.google.gson.internal

import androidx.collection.SparseArrayCompat
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

/**
 * Created on 2020/4/24
 * Author: bigwang
 * Description:
 */
object KWGsonTypes {

    fun getSparseArrayCompatType(context: Type, contextRawType: Class<*>): Type {
        var collectionType = `$Gson$Types`.getSupertype(
            context, contextRawType,
            SparseArrayCompat::class.java
        )

        if (collectionType is WildcardType) {
            collectionType =
                collectionType.upperBounds[0]
        }
        return if (collectionType is ParameterizedType) {
            collectionType.actualTypeArguments[0]
        } else Any::class.java
    }

}