package com.wang.idcardgenerate

import java.io.Closeable

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
fun Closeable?.quietClose() {
    try {
        this?.close()
    } catch (e: Throwable) {

    }

}