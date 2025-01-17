package com.mickie.project.app // Package declaration for organizing the code

import android.util.Log // Importing the Log class for logging messages
import kotlinx.coroutines.Dispatchers // Importing Dispatchers for coroutine context
import kotlinx.coroutines.runBlocking // Importing runBlocking for blocking coroutine execution
import kotlinx.coroutines.withContext // Importing withContext for changing coroutine context
import kotlinx.serialization.decodeFromString // Importing the function for decoding JSON strings
import kotlinx.serialization.json.Json // Importing the Json class for JSON parsing
import okhttp3.OkHttpClient // Importing OkHttpClient for making HTTP requests
import okhttp3.Request // Importing Request class for constructing HTTP requests
import kotlinx.serialization.Serializable // Importing Serializable annotation for Kotlin serialization



// Main class for fetching Formula 1 driver standings
class F1Fetch {

    // Data class representing the entire response from the API
    @Serializable
    data class DriverStandingsResponse(
        val MRData: MRData // Property representing the MRData object
    )

    // Data class representing the MRData part of the API response
    @Serializable
    data class MRData(
        val xmlns: String, // XML namespace (can be ignored)
        val series: String, // The series (F1)
        val url: String, // URL for the API request
        val limit: String, // Limit of records fetched
        val offset: String, // Offset for pagination
        val total: String, // Total number of records
        val StandingsTable: StandingsTable // Property for standings table
    )

    // Data class representing the standings table
    @Serializable
    data class StandingsTable(
        val season: String, // Season year
        val StandingsLists: List<StandingsList> // List of standings lists
    )

    // Data class representing each standings list
    @Serializable
    data class StandingsList(
        val season: String, // Season year
        val round: String, // Round number
        val DriverStandings: List<DriverStanding> // List of driver standings
    )

    // Data class representing individual driver standings
    @Serializable
    data class DriverStanding(
        val position: String, // Driver's position in the standings
        val positionText: String, // Text representation of position
        val points: String, // Points earned
        val wins: String, // Number of wins
        val Driver: Driver, // Driver details
        val Constructors: List<Constructor> // List of constructors
    )

    // Data class representing driver details
    @Serializable
    data class Driver(
        val driverId: String, // Unique driver ID
//        val permanentNumber: String?, // Permanent racing number
        val code: String, // Driver code
        val url: String, // URL for driver's Wikipedia page
        val givenName: String, // Driver's given name
        val familyName: String, // Driver's family name
        val dateOfBirth: String, // Driver's date of birth
        val nationality: String // Driver's nationality
    )

    // Data class representing constructor details
    @Serializable
    data class Constructor(
        val constructorId: String, // Unique constructor ID
        val url: String, // URL for constructor's Wikipedia page
        val name: String, // Constructor name
        val nationality: String // Constructor nationality
    )

    // Function to fetch driver standings asynchronously
    suspend private fun fetchDriverStandings(year : Int): DriverStandingsResponse? {
        val client = OkHttpClient() // Create an instance of OkHttpClient

        // Build the HTTP request
        val request = Request.Builder()
            .url("https://ergast.com/api/f1/${year}/driverStandings.json") // API endpoint
            .build()

        // JSON configuration to ignore unknown keys
        val json = Json {
            ignoreUnknownKeys = true // This will ignore unknown fields like 'xmlns'
        }

        // Execute the request in the IO coroutine context
        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                // Check if the response is successful
                if (response.isSuccessful) {
                    val responseData = response.body?.string() // Get the response body as a string
                    responseData?.let {
                        // Decode the JSON string into DriverStandingsResponse object
                        json.decodeFromString<DriverStandingsResponse>(it)
                    }
                } else {
                    null // Return null if the request was not successful
                }
            }
        }
    }

    // Function to fetch and log driver standings
    suspend fun fetchAndLogDriverStandings( year: Int):DriverStandingsResponse? {
        val standings = fetchDriverStandings(year) // Call the fetch function
        // Safely access DriverStandings and log their details
        standings?.MRData?.StandingsTable?.StandingsLists?.firstOrNull()?.DriverStandings?.let { driverStandings ->
            driverStandings.forEach { standing ->
                // Log driver position, name, and constructor
                Log.println(
                    Log.INFO,
                    "Fetch",
                    "Position: ${standing.position}, Driver: ${standing.Driver.givenName} ${standing.Driver.familyName}, Constructor: ${standing.Constructors.first().name}, Point: ${standing.points}"
                )
            }
        } ?: Log.println(Log.ERROR, "Fetch", "Failed to fetch data or DriverStandings is null.")
        // Log an error if standings are null
        return standings
    }
}
