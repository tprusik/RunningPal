package com.example.runningpal.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.tests.repositories.FakeRunRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RunRepository {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    var fakeRunRepository = FakeRunRepository()


    @Test
    fun return_total_run_statistics_type() {

      var value =  fakeRunRepository.getTotalStatistics("1")
       Assert.assertTrue(value.value is RunStatistics)
    }
}