
package com.amarhisab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amarhisab.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val dao: AppDao) : ViewModel() {

    val profile = dao.getProfile().stateIn(viewModelScope, SharingStarted.Lazily, null)
    val transactions = dao.getAllTransactions().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val savings = dao.getAllSavings().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val persons = dao.getAllPersons().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Calculations for Dashboard
    val totalIncome = transactions.map { list ->
        list.filter { it.type == "INCOME" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense = transactions.map { list ->
        list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balance = combine(totalIncome, totalExpense) { inc, exp -> inc - exp }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addTransaction(type: String, amount: Double, category: String, note: String) {
        viewModelScope.launch {
            dao.insertTransaction(Transaction(type = type, amount = amount, category = category, note = note, date = System.currentTimeMillis()))
        }
    }

    fun addPerson(name: String) {
        viewModelScope.launch {
            dao.insertPerson(Person(name = name))
        }
    }

    fun addPersonTransaction(personId: Long, type: String, amount: Double, note: String) {
        viewModelScope.launch {
            dao.insertPersonTransaction(PersonTransaction(personId = personId, type = type, amount = amount, note = note, date = System.currentTimeMillis()))
        }
    }

    fun updateSavings(amount: Double, note: String) {
        viewModelScope.launch {
            dao.insertSavings(SavingsEntry(amount = amount, note = note, date = System.currentTimeMillis()))
        }
    }

    fun updateProfile(name: String, email: String, photoUri: String?) {
        viewModelScope.launch {
            dao.updateProfile(Profile(name = name, email = email, photoUri = photoUri))
        }
    }
}
