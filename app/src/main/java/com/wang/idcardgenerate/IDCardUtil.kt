package com.wang.idcardgenerate

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
object IDCardUtil {

    private val FACTOR = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
    private val SUFFIX = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')


    fun getIdCardLast(
        areaCode: Int,
        year: Int,
        moth: Int,
        dayOfMoth: Int,
        sort1: Int,
        sort2: Int,
        sex: Int
    ): Char {
        var areaCode = areaCode
        var weightSum = 0
        for (i in 5 downTo 0) {
            weightSum += ((areaCode % 10) * FACTOR[i])
            areaCode /= 10
        }
        var year = year
        for (i in 9 downTo 6){
            weightSum += ((year % 10) * FACTOR[i])
            year /= 10
        }

        var moth = moth
        for (i in 11 downTo 10){
            weightSum += ((moth % 10) * FACTOR[i])
            moth /= 10
        }

        var dayOfMoth = dayOfMoth
        for (i in 13 downTo 12){
            weightSum += ((dayOfMoth % 10) * FACTOR[i])
            dayOfMoth /= 10
        }
        weightSum += sort1 * FACTOR[14]
        weightSum += sort2 * FACTOR[15]
        weightSum += sex * FACTOR[16]

        return SUFFIX[weightSum % 11]
    }
}