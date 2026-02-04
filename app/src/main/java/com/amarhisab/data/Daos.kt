
package com.amarhisab.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Profile
    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfile(): Flow<Profile?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProfile(profile: Profile)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    @Insert
    suspend fun insertTransaction(transaction: Transaction)
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // Savings
    @Query("SELECT * FROM savings_entries ORDER BY date DESC")
    fun getAllSavings(): Flow<List<SavingsEntry>>
    @Insert
    suspend fun insertSavings(entry: SavingsEntry)

    // Persons & Dues
    @Query("SELECT * FROM persons")
    fun getAllPersons(): Flow<List<Person>>
    @Insert
    suspend fun insertPerson(person: Person)
    @Query("SELECT * FROM person_transactions WHERE personId = :personId ORDER BY date DESC")
    fun getTransactionsForPerson(personId: Long): Flow<List<PersonTransaction>>
    @Insert
    suspend fun insertPersonTransaction(pt: PersonTransaction)
}
