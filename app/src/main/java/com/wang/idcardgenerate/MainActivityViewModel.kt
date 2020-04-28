package com.wang.idcardgenerate

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.SparseArrayCompatTypeAdapterFactory
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Created on 2020/4/23
 * Author: bigwang
 * Description:
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    val mAreaLiveData: MutableLiveData<AreaData> by lazy { MutableLiveData<AreaData>() }

    private var mArea: AreaData? = null

    fun getArea() {
        mArea?.let {
            mAreaLiveData.value = it
        } ?: let {
            viewModelScope.launch {
                val gson =
                    GsonBuilder().registerTypeAdapterFactory(SparseArrayCompatTypeAdapterFactory())
                        .create()

                val result = getApplication<Application>().assets.runCatching {
                    AreaData(
                        LocalRepository.getProvince(this, gson),
                        LocalRepository.getArea(this, "city.json", gson),
                        LocalRepository.getArea(this, "county.json", gson)
                    )

                }
                if (result.isFailure) {
                    Log.e("error", "", result.exceptionOrNull())
                } else {
                    mArea = result.getOrNull()
                    mAreaLiveData.value = mArea
                }
            }
        }
    }

    fun getIdCard(areaCode: Int, year: Int, moth: Int, dayOfMoth: Int, sex: Int): String {
        val idCard =
            StringBuilder().append(areaCode).append(String.format("%4d", year)).append(String.format("%02d", moth))
                .append(String.format("%02d", dayOfMoth))
        val sort1 = Random.nextInt(10)
        val sort2 = Random.nextInt(10)
        val realSex = 2 * (Random.nextInt(5)) + sex
        return idCard.append(sort1).append(sort2).append(realSex).append(
            IDCardUtil.getIdCardLast(
                areaCode,
                year,
                moth,
                dayOfMoth,
                sort1,
                sort2,
                realSex
            )
        ).toString()
    }

}