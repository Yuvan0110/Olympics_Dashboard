package com.example.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dashboard.data.Country
import com.example.dashboard.data.Event
import com.example.dashboard.data.MedalType
import com.example.dashboard.repository.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = EventsRepository()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events : StateFlow<List<Event>> = _events


    private val _selectedMedalType = MutableStateFlow(MedalType.TOTAL)
    val selectedMedalType: StateFlow<MedalType> = _selectedMedalType

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries : StateFlow<List<Country>> = _countries

    fun loadEvents(page : Int) {
        viewModelScope.launch {
            val response = repository.fetchEvents(page)
            response?.data?. let {
                _events.value = it
            }
        }
    }

    fun loadCountries() {
        viewModelScope.launch {
            val response = repository.fetchCountries()
            response?.data?. let {
                _countries.value = it
            }
        }
    }

    fun selectMedalType(type: MedalType) {
        _selectedMedalType.value = type
    }
}