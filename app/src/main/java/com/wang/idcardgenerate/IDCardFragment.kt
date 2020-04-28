package com.wang.idcardgenerate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.wang.wheel.model.DataModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_id_card.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class IDCardFragment : Fragment(), OnAreaSelectListener {

    private lateinit var viewModel: MainActivityViewModel

    private var mProvinceId = 0
    private var mCityId = 0
    private var mAreaId = 0

    private var mYear = 0
    private var mMoth = 0
    private var mDayOfMoth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(MainActivityViewModel::class.java)

        viewModel.getArea()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_id_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mAreaLiveData.observe(viewLifecycleOwner, Observer {
            val area = StringBuilder()
            it.mProvince[0].run {
                mProvinceId = id
                area.append(name)
            }
            it.mCity[mProvinceId]?.get(0)?.run {
                mCityId = id
                area.append(" ").append(name)
            }
            it.mArea[mCityId]?.get(0)?.run {
                mAreaId = id
                area.append(" ").append(name)
            }
            area_result_tv.text = area
        })

        area_result_tv.setOnClickListener {
            findNavController().navigate(
                R.id.area_dialog,
                AreaDialogArgs(mProvinceId, mCityId, mAreaId).toBundle()
            )
        }

        val now = Calendar.getInstance()
        mYear = now.get(Calendar.YEAR)
        mMoth = now.get(Calendar.MONTH)
        mDayOfMoth = now.get(Calendar.DAY_OF_MONTH)
        birthday_result_tv.text = "${mYear}-${mMoth + 1}-${mDayOfMoth}"

        birthday_result_tv.setOnClickListener {
            DatePickerDialog.newInstance({ _, year, monthOfYear, dayOfMonth ->
                mYear = year
                mMoth = monthOfYear
                mDayOfMoth = dayOfMonth
                birthday_result_tv.text = "$year-${monthOfYear + 1}-$dayOfMonth"
            }, mYear, mMoth, mDayOfMoth).apply {
                version = DatePickerDialog.Version.VERSION_2
                show(this@IDCardFragment.childFragmentManager, "DatePickerDialog")
            }
        }

        sex_spinner.adapter = object : ArrayAdapter<DataModel>(
            requireContext(),
            R.layout.item_sex,
            ArrayList<DataModel>().apply {
                add(object : DataModel(1, "男") {
                    override fun toString(): String {
                        return name
                    }
                })
                add(object : DataModel(0, "女") {
                    override fun toString(): String {
                        return name
                    }
                })
            }) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return super.getView(position, convertView, parent).apply { isActivated = true }
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent)
                    .apply { isSelected = true }
            }
        }

        generate_fab.setOnClickListener {
            result_tv.text = viewModel.getIdCard(mAreaId, mYear, mMoth + 1, mDayOfMoth, (sex_spinner.selectedItem as DataModel).id)
        }

    }

    override fun onAreaSelected(province: DataModel?, city: DataModel?, area: DataModel?) {
        val areaResult = StringBuilder()
        province?.also {
            mProvinceId = it.id
            areaResult.append(it.name)
        }

        city?.also {
            mCityId = it.id
            areaResult.append(" ").append(it.name)
        }

        area?.also {
            mAreaId = it.id
            areaResult.append(" ").append(it.name)
        }
        area_result_tv.text = areaResult
    }
}
