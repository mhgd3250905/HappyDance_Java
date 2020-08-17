package com.example.happydance_java.db.dao;

import com.example.happydance_java.db.Position;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PositionDao {
    @Query("SELECT * FROM position")
    LiveData<List<Position>> queryAll();

    @Query("SELECT * FROM position")
    List<Position> queryAll2();
//
//    @Query("SELECT * FROM position WHERE id IN (:ids)")
//    MutableLiveData<List<Position>> queryByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Position... positions);

    @Delete
    void delete(Position... position);

    @Query("DELETE FROM Position")
    void deleteAll();

    @Update
    void update(Position... positions);
}
