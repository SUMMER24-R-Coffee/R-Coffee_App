package datlowashere.project.rcoffee.data.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class Voucher(
    val voucher_id: Int,
    val voucher_code: String,
    val percent: Double,
    val valid_date: Date,
    val exp_date: Date,
    val isVisible: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(voucher_id)
        parcel.writeString(voucher_code)
        parcel.writeDouble(percent)
        parcel.writeLong(valid_date.time)
        parcel.writeLong(exp_date.time)
        parcel.writeByte(if (isVisible) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Voucher> {
        override fun createFromParcel(parcel: Parcel): Voucher {
            return Voucher(parcel)
        }

        override fun newArray(size: Int): Array<Voucher?> {
            return arrayOfNulls(size)
        }
    }
}
