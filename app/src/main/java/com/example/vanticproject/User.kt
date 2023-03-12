import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Parcelize
data class User(
    @Expose
    @SerializedName("email") val  email: String,

    @Expose
    @SerializedName("password") val  password: String,

    @Expose
    @SerializedName("fname") val  fname: String,

    @Expose
    @SerializedName("lname") val  lname: String,

    @Expose
    @SerializedName("phone") val  phone: String,

    @Expose
    @SerializedName("usertypeid") val  usertypeid: Int
) : Parcelable {}