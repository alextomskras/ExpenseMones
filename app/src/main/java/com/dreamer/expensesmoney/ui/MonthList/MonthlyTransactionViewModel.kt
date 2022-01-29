package com.dreamer.expensesmoney.ui.MonthList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dreamer.expensesmoney.data.MonthlyTransactions
import com.dreamer.expensesmoney.data.TransactionListRepository

class MonthlyTransactionViewModel(application: Application): AndroidViewModel(application) {

    private val repo: TransactionListRepository = TransactionListRepository(application)

    val month: LiveData<List<MonthlyTransactions>>
        get() = repo.getTransactionMonth()

}