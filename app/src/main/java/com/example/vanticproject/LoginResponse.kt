import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("success") val success: Int,

    @Expose
    @SerializedName("email") val email: String,

    @Expose
    @SerializedName("fname") val fname: String,

    @Expose
    @SerializedName("lname") val lname: String,

    @Expose
    @SerializedName("phone") val phone: String,

    @Expose
    @SerializedName("password") val password: String,

    @Expose
    @SerializedName("usertypeid") val usertypeid: Int,
    ) {}