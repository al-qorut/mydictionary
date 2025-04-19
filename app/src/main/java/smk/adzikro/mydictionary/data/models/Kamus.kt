package smk.adzikro.mydictionary.data.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kamus")
data class Kamus(
    @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = "id")
    var id : Int = 0,
    var kata : String,
    var arti : String,
    var root : String = "",
    var source : String = "",
    var favorite : Int = 0
)
