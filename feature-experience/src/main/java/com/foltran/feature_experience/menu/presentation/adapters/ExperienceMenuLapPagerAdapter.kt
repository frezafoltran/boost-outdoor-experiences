package com.foltran.feature_experience.menu.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_utils.extensions.round
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.core_utils.formatting.timeFromSecondsToMinSeconds
import com.foltran.feature_experience.databinding.ItemExperienceMenuLapSelectorBinding

data class LapSelectorData(
    val name: String,
    val distance: Double?,
    val time: Int?
) {
    val formattedDistance = distance?.meterToKm()
    val formattedTime = time?.timeFromSecondsToMinSeconds()
}

class LapSelectorViewHolder private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: LapSelectorData) {

        when(binding){
            is ItemExperienceMenuLapSelectorBinding -> {
                binding.lapSelectorData = data
            }
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): LapSelectorViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return LapSelectorViewHolder(
                ItemExperienceMenuLapSelectorBinding.inflate(inflater, parent, false)
            )
        }
    }
}

abstract class BaseAdapter : RecyclerView.Adapter<LapSelectorViewHolder>() {

    override fun onBindViewHolder(holder: LapSelectorViewHolder, position: Int) {
        // Do nothing
    }
}

class ExperienceMenuLapPagerAdapter(val items: List<LapSelectorData>) : BaseAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapSelectorViewHolder {
        return LapSelectorViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: LapSelectorViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
