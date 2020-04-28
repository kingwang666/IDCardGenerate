package com.wang.idcardgenerate

import androidx.collection.SparseArrayCompat
import com.wang.wheel.model.DataModel

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
data class AreaData(
    val mProvince: List<DataModel>,
    val mCity: SparseArrayCompat<List<DataModel>>,
    val mArea: SparseArrayCompat<List<DataModel>>
)