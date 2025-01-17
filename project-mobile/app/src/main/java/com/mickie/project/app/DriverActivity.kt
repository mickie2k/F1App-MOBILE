package com.mickie.project.app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mickie.project.R
import com.mickie.project.databinding.ActivityDriverBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DriverActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: List<F1Fetch.DriverStanding>
    private var year: Int = 2024

    private val binding: ActivityDriverBinding by lazy {
        ActivityDriverBinding.inflate(layoutInflater)
    }

    companion object {
        private const val YEAR = "year"

        fun newIntent(
            context: Context,
            year: Int
        ): Intent {
            return Intent(context, DriverActivity::class.java).apply {
                putExtra(YEAR, year)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val adapter = ReviewAdapter()
//        binding.recyclerViewReview.layoutManager = LinearLayoutManager(this)
//        binding.recyclerViewReview.adapter = adapter
//
//        val f1Driver = F1Fetch()

        restoreBundle()

        binding.year.text = "$year"
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = listOf()

        // Use a coroutine to fetch data asynchronously
        lifecycleScope.launch(Dispatchers.Main) {
            getData(year)
        }
    }

    private fun restoreBundle() {
        year = intent.getIntExtra(YEAR, 2024)
    }


    private suspend fun getData(year: Int) {
        val f1Driver = F1Fetch()
        val standingsResponse: F1Fetch.DriverStandingsResponse? = withContext(Dispatchers.IO) {
            f1Driver.fetchAndLogDriverStandings(year)
        }

        // Once data is fetched, update the UI on the main thread
        standingsResponse?.let { response ->
            dataList = response.MRData.StandingsTable.StandingsLists.firstOrNull()?.DriverStandings.orEmpty()
            // Update the RecyclerView adapter with the new data
            recyclerView.adapter = DriverAdapter(dataList)
        } ?: run {
            // Handle the case where data fetching failed
            // You can show a Toast or a log message here
        }
    }
}
