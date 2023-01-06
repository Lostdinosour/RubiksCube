package me.rubik.rubikscube.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Times {

    @PrimaryKey(autoGenerate = true)

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "time")
    public int time;


    public void setValues(long date, int solveTime) {
        this.date = date;
        this.time = solveTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "{time:" + time + ", data:" + date + "}";
    }

}

