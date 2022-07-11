package com.foltran.feature_experience.record.presentation

import android.os.Bundle

import android.view.View
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.feature_experience.databinding.FragmentExperienceStartRecordBinding
import com.foltran.feature_experience.record.di.ExperienceRecordModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.foltran.feature_experience.R


class ExperienceStartRecordFragment : BaseFragment(R.layout.fragment_experience_start_record, ExperienceRecordModule.instance) {

    private val viewModel: ExperienceStartRecordViewModel by viewModel()
    private val binding: FragmentExperienceStartRecordBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        setupSpinner()
    }


    fun setupSpinner(){
        val years = arrayOf("Running", "Biking", "Hiking", "Skiing")
        val langAdapter = ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_text, years)
        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        binding.spinnerSelectExperienceType.setAdapter(langAdapter)

        binding.imageViewDropDownIcon.setOnClickListener{
            binding.spinnerSelectExperienceType.performClick()
        }
    }

    companion object {

        fun newInstance() =
            ExperienceStartRecordFragment()
    }
}