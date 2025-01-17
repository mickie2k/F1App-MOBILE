package com.mickie.project.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mickie.project.databinding.ActivityMainBinding


import java.util.Calendar


class MainActivity : AppCompatActivity() {




    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
//        val adapter = ReviewAdapter()
//        binding.recyclerViewReview.layoutManager = LinearLayoutManager(this)
//        binding.recyclerViewReview.adapter = adapter
//
//        val f1Driver = F1Fetch()
//        f1Driver.fetch()

//        binding.recyclerView.layoutManager = LinearLayoutManager (this)
//        binding.recyclerView.setHasFixedSize(true)
//        dataList = listOf<F1Fetch.DriverStanding>()
//        getData()
    }

    private fun setupView(){
        val myConstraintLayout = binding.driversButton
        myConstraintLayout.setOnClickListener {
            // Handle click event
            launchDriverActivity()
        }



        
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.yearPickerLayout.minValue = 2005
        binding.yearPickerLayout.maxValue = currentYear
        binding.yearPickerLayout.value = currentYear


    }

    private fun launchDriverActivity() {
        val selectedYear = binding.yearPickerLayout.value
        binding.selectedYearTextView.text = "Selected Year: $selectedYear"

        // Launch the activity without blocking the UI thread
        val intent = DriverActivity.newIntent(this, selectedYear)
        startActivity(intent)
    }



//    private fun getData(){
//        val f1Driver = F1Fetch()
//        val  standingsResponse: F1Fetch.DriverStandingsResponse? = f1Driver.fetch()
//        if (standingsResponse != null) {
//            dataList =
//                standingsResponse.MRData.StandingsTable.StandingsLists.firstOrNull()?.DriverStandings!!
//        }
//        binding.recyclerView.adapter = DriverAdapter(dataList)
//    }
}
