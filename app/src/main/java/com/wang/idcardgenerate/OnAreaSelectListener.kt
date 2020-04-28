package com.wang.idcardgenerate

import com.wang.wheel.model.DataModel

/**
 * Created on 2020/4/24
 * Author: bigwang
 * Description:
 */
interface OnAreaSelectListener {

    fun onAreaSelected(province: DataModel?, city: DataModel?, area: DataModel?)

}