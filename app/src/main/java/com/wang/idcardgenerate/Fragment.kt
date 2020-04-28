package com.wang.idcardgenerate

import androidx.fragment.app.Fragment

/**
 * Created on 2020/4/24
 * Author: bigwang
 * Description:
 */

fun <T> Fragment.findCallback(clazz: Class<T>): T? {
    parentFragment?.also {
        if (clazz.isAssignableFrom(it.javaClass)){
            return clazz.cast(it)
        }
    }
    activity?.also {
        if (clazz.isAssignableFrom(it.javaClass)){
            return clazz.cast(it)
        }
    }
    return null
}

fun <T> Fragment.findActivityCallback(clazz: Class<T>): T? {
    activity?.also {
        if (clazz.isAssignableFrom(it.javaClass)){
            return clazz.cast(it)
        }
    }
    return null
}

fun <T> Fragment.findParentFragmentCallback(clazz: Class<T>): T? {
    parentFragment?.also {
        if (clazz.isAssignableFrom(it.javaClass)){
            return clazz.cast(it)
        }
    }
    return null
}

fun <T> Fragment.findFragmentCallback(fragment: Class<out Fragment>, callback: Class<T>): T? {
    parentFragment?.also {
        it.childFragmentManager.fragments.find { current ->
            fragment.isAssignableFrom(current.javaClass) && callback.isAssignableFrom(current.javaClass)
        }?.run {
            return callback.cast(this)
        }
    }
    activity?.also {
        it.supportFragmentManager.fragments.find { current ->
            fragment.isAssignableFrom(current.javaClass) && callback.isAssignableFrom(current.javaClass)
        }?.run {
            return callback.cast(this)
        }
    }
    return null
}