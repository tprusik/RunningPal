package com.example.runningpal.ui.viewmodels


import androidx.lifecycle.*
import com.example.runningpal.db.Run
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.repositories.IRunRepository
import com.example.runningpal.repositories.IRunStatisticsRepository
import kotlinx.coroutines.launch

class StatisticsViewModel(val repo : IRunStatisticsRepository)  : ViewModel() {

    fun getRunStatistics(id : String) : LiveData<List<RunStatistics>> = repo.getRunStatistics(id)

    val runs = MutableLiveData<List<RunStatistics>>()

    fun saveRunStatistics(runStatistics: RunStatistics) = viewModelScope.launch { repo.insertRunStatistics(runStatistics) }

}