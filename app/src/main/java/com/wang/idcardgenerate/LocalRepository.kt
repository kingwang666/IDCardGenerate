package com.wang.idcardgenerate

import android.content.res.AssetManager
import androidx.collection.SparseArrayCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wang.wheel.model.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
object LocalRepository {

    @Throws(IOException::class)
    suspend fun getProvince(asset: AssetManager, gson: Gson): List<DataModel> {
        return withContext(Dispatchers.Default) {
            asset.open("province.json").use {
                gson.fromJson<List<DataModel>>(
                    InputStreamReader(it),
                    TypeToken.getParameterized(List::class.java, DataModel::class.java).type
                )
            }
        }
    }

    @Throws(IOException::class)
    suspend fun getArea(
        asset: AssetManager,
        name: String,
        gson: Gson
    ): SparseArrayCompat<List<DataModel>> {
        return withContext(Dispatchers.Default) {
            asset.open(name).use {
                gson.fromJson<SparseArrayCompat<List<DataModel>>>(
                    InputStreamReader(it),
                    TypeToken.getParameterized(
                        SparseArrayCompat::class.java,
                        TypeToken.getParameterized(List::class.java, DataModel::class.java).type
                    ).type
                )
            }
        }
    }
}