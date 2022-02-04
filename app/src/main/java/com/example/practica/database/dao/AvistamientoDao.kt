package com.example.practica.database.dao

import androidx.room.*
import com.example.practica.database.entidades.Avistamiento

@Dao
interface AvistamientoDao {

    @Query("SELECT * FROM Avistamiento")
    fun getAllAvistamientos() : List<Avistamiento>

    @Query("SELECT * FROM Avistamiento WHERE id = :id")
    fun getAvistamientoById(id: Int) : Avistamiento

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAvistamiento(avistamiento: Avistamiento)

    @Update
    fun updateAvistamiento(avistamiento: Avistamiento)

    @Delete
    fun deleteAvistamiento(avistamiento: Avistamiento)

    @Query("DELETE FROM Avistamiento")
    fun cleanDB()
}