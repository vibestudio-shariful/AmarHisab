
package com.amarhisab.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String? = null,
    val photoUri: String? = null
)

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "INCOME" or "EXPENSE"
    val amount: Double,
    val category: String,
    val note: String,
    val date: Long // Timestamp
)

@Entity(tableName = "savings_entries")
data class SavingsEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double, // Positive for deposit, negative for withdraw
    val note: String,
    val date: Long
)

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val note: String? = null
)

@Entity(tableName = "person_transactions")
data class PersonTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val type: String, // "GAVE" or "RECEIVED"
    val amount: Double,
    val note: String,
    val date: Long
)
