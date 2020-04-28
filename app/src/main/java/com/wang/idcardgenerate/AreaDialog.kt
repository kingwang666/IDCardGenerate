package com.wang.idcardgenerate

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.wang.wheel.listener.OnButtonClickListener
import com.wang.wheel.model.DataModel
import kotlinx.android.synthetic.main.dialog_area.*

/**
 * Created on 2020/4/24
 * Author: bigwang
 * Description:
 */
class AreaDialog : AppCompatDialogFragment() {

    private lateinit var mViewModel: MainActivityViewModel
    private val mArgs: AreaDialogArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.YDBottomDialog)
        mViewModel = ViewModelProvider(requireActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(MainActivityViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pick_view.setButtonClickListener(object : OnButtonClickListener{
            override fun onClick(province: DataModel?, city: DataModel?, area: DataModel?) {
                findFragmentCallback(IDCardFragment::class.java, OnAreaSelectListener::class.java)?.onAreaSelected(province, city, area)
                dismiss()
            }

            override fun onCancel() {
                dismiss()
            }

        })
        mViewModel.mAreaLiveData.observe(viewLifecycleOwner, Observer {
            pick_view.setDefault(mArgs.provinceId, mArgs.cityId, mArgs.areaId)
            pick_view.setProvince(it.mProvince)
            pick_view.setCity(it.mCity)
            pick_view.setArea(it.mArea)
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setGravity(Gravity.BOTTOM)
        }
    }
}