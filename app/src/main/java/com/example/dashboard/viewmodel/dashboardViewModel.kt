package com.example.dashboard.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard.data.CountryStats
import com.example.dashboard.data.Country
import com.example.dashboard.data.TableEntry
import com.example.dashboard.network.RetrofitInstance
import com.example.dashboard.repository.DashboardsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.iterator

class DashboardViewModel : ViewModel() {

    private val repository = DashboardsRepository()

    // List countries
    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries : StateFlow<List<Country>> = _countries
    fun loadCountries() {
        viewModelScope.launch {
            val response = repository.fetchCountries()
            response?.data?. let {
                _countries.value = it
            }
        }
    }

    // Discipline - country
    private val _disciplineCountryMap = MutableStateFlow<MutableMap<String, MutableSet<String>>>(mutableMapOf())
    val disciplineCountryMap: StateFlow<MutableMap<String, MutableSet<String>>> = _disciplineCountryMap

    private val _countryDisciplineMap = MutableStateFlow<MutableMap<String, MutableSet<String>>>(mutableMapOf())
    val countryDisciplineMap: StateFlow<MutableMap<String, MutableSet<String>>> = _countryDisciplineMap

    fun loadDisciplineCountries() {
        viewModelScope.launch {
            val response = repository.fetchAllEvents()
            val lastPage = response?.meta?.lastPage ?: 0

            val tempDisciplineMap = mutableMapOf<String, MutableSet<String>>()

            for (page in 1..5) {
                val res = RetrofitInstance.api.getEvents(page)
                val events = res.body()?.data ?: continue

                for (event in events) {
                    val key = event.disciplineName?: continue
                    val countries = event.competitors.mapNotNull { it.countryId }
                    val set = tempDisciplineMap.getOrPut(key) { mutableSetOf() }
                    set.addAll(countries)
                }
            }
            _disciplineCountryMap.value = tempDisciplineMap

            val tempCountryMap = mutableMapOf<String, MutableSet<String>>()

            for ((discipline, countries) in tempDisciplineMap) {
                for (country in countries) {
                    val set = tempCountryMap.getOrPut(country) { mutableSetOf() }
                    set.add(discipline)
                }
            }
            _countryDisciplineMap.value = tempCountryMap
        }
    }

    // events grouped by men and women
    private val _menEvents = MutableStateFlow(0)
    val menEvents: StateFlow<Int> = _menEvents

    private val _womenEvents = MutableStateFlow(0)
    val womenEvents: StateFlow<Int> = _womenEvents

    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded

    fun eventsGroupedByMenAndWomen() {
        viewModelScope.launch {
            _menEvents.value = 0
            _womenEvents.value = 0
            _isDataLoaded.value = false

            val lastPage = repository.fetchAllEvents()?.meta?.lastPage ?: 0

            for (page in 1..10) {
                val res = RetrofitInstance.api.getEvents(page)
                val events = res.body()?.data ?: continue

                for (event in events) {
                    if ((event.genderCode ?: "") == "M") {
                        _menEvents.value += 1
                    } else {
                        _womenEvents.value += 1
                    }
                }
            }

            _isDataLoaded.value = true
        }
    }


    // table ranking
    private val _tableRanking = MutableStateFlow<List<TableEntry>>(mutableListOf())
    val tableRanking : StateFlow<List<TableEntry>> = _tableRanking
    fun getTableRanking() {
        viewModelScope.launch {
            val response = repository.fetchCountries()
            val countries = response?.data?:emptyList()

            val tempList = countries.map {
                TableEntry(it.id, it.totalMedals, it.rank, it.rankTotalMedals)
            }
            _tableRanking.value = tempList
        }
    }

    // events participated - medals won ratio
    private val _countryStats = MutableStateFlow<List<CountryStats>>(emptyList())
    val countryStats: StateFlow<List<CountryStats>> = _countryStats

    fun loadCountryStats() {
        viewModelScope.launch {
            val countries = mutableListOf<Country>()
            val eventsByCountry = mutableMapOf<String, Int>()

            val response = RetrofitInstance.api.getCountries()
            countries += response.body()?.data ?: emptyList()

            for (page in 1..10) {
                val response = RetrofitInstance.api.getEvents(page)
                val events = response.body()?.data ?: continue
                for (event in events) {
                    event.competitors.forEach { competitor ->
                        val countryId = competitor.countryId ?: ""
                        eventsByCountry[countryId] = eventsByCountry.getOrDefault(countryId, 0) + 1
                        Log.d("MapEntries", "${eventsByCountry[countryId]} $countryId")
                    }
                    Log.d("Debug", "Event ${event.id} has ${event.competitors.size} competitors")
                }
            }
            val stats = countries.map { country ->
                val id = country.name ?: ""
                val events = eventsByCountry[id] ?: 0
                Log.d("Country", "${country.id} $events")
                CountryStats(
                    countryId = id,
                    name = country.name ?: "",
                    totalMedals = country.totalMedals ?: 0,
                    eventsParticipated = events,
                    medalRatio = if (events > 0) (country.totalMedals?.toFloat() ?: 0f) / events else 0f
                )
            }
            _countryStats.value = stats
        }
    }
}