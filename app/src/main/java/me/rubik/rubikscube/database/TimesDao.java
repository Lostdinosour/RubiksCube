package me.rubik.rubikscube.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimesDao {
    @Query("SELECT * FROM times")
    List<Times> getAll();

    @Insert
    void insert(Times time);

    @Delete
    void delete(Times time);

    @Query("SELECT time FROM times")
    List<Integer> getAllTimes();

    @Query("SELECT MIN(time) FROM times")
    int getBestTime();

    @Query("DELETE FROM times")
    void deleteAll();

}
