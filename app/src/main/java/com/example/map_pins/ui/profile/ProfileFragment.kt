package com.example.map_pins.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_pins.R
import com.example.map_pins.data.db.AppDB
import com.example.map_pins.data.repository.PinRepository
import com.example.map_pins.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(),
    RecyclerViewAdapter.OnItemClickListener{

    lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDB : AppDB = AppDB(requireContext())
        val pinRepository : PinRepository = PinRepository(appDB)
        var recyclerViewAdapter =
            RecyclerViewAdapter(
                listOf(),
                pinRepository,
                this,
                requireContext()
            )

        binding.recyclerPins.layoutManager = LinearLayoutManager(this.activity);
        binding.recyclerPins.adapter = recyclerViewAdapter

        var pins = pinRepository.getAllPins()
        Log.d("MyTag", pins.toString())
        recyclerViewAdapter.replaceData(pins)

    }

    override fun onItemClick(position: Int) {

    }
}