package com.example.happydance_java.db.dao;

import com.example.happydance_java.db.Position;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PositionDao {
    @Query("SELECT * FROM position")
    List<Position> queryAll();

    @Query("SELECT * FROM position WHERE id IN (:ids)")
    List<Position> queryByIds(int[] ids);

    @Insert
    void insert(Position... positions);

    @Delete
    void delete(Position position);
}
