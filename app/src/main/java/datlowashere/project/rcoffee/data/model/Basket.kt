package datlowashere.project.rcoffee.data.model

import android.os.Parcel
import android.os.Parcelable

data class Basket(
    val basket_id: Int = 0,
    val categoryName: String = "",
    val name: String = "",
    val img: String = "",
    val price: Double = 0.0,
    var quantity: Int = 0,
    val product_id: Int = 0,
    val email_user: String = "",
    val order_id: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(basket_id)
        parcel.writeString(categoryName)
        parcel.writeString(name)
        parcel.writeString(img)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
        parcel.writeInt(product_id)
        parcel.writeString(email_user)
        parcel.writeString(order_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Basket> {
        override fun createFromParcel(parcel: Parcel): Basket {
            return Basket(parcel)
        }

        override fun newArray(size: Int): Array<Basket?> {
            return arrayOfNulls(size)
        }
    }
}
