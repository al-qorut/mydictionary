package smk.adzikro.mydictionary.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kamus")
class Kamus(
    @PrimaryKey @NonNull
    var _id : Int,
    var kata : String,
    var arti : String
)