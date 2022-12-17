package me.rubik.rubikscube.database;

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

    @ColumnInfo(name = "scramble")
    public String scramble;

    public void setValues(long date, int solveTime, String scramble) {
        this.date = date;
        this.time = solveTime;
        this.scramble = scramble;
    }

}

