package com.example.happydance_java.db;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Position {
    @PrimaryKey(autoGenerate = true)
    public @NonNull int id;

    @ColumnInfo(name = "latitude")
    public @NonNull double latitude;

    @ColumnInfo(name = "longitude")
    public @NonNull double longitudu;

    @ColumnInfo(name = "create_time")
    public @NonNull Date createTime;

    public Position(double latitude, double longitudu, Date createTime) {
        this.latitude = latitude;
        this.longitudu = longitudu;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitudu() {
        return longitudu;
    }

    public void setLongitudu(double longitudu) {
        this.longitudu = longitudu;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitudu=" + longitudu +
                ", createTime=" + createTime +
                '}';
    }
}
