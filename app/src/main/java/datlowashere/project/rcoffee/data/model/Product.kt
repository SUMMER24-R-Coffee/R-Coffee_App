package datlowashere.project.rcoffee.data.model

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val product_id: Int,
    val product_name: String,
    val img: String?,
    val description: String?,
    val price: Double,
    val category_id: Int,
    val average_rating: Double,
    val favourite_id: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(product_id)
        parcel.writeString(product_name)
        parcel.writeString(img)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(category_id)
        parcel.writeDouble(average_rating)
        parcel.writeValue(favourite_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
