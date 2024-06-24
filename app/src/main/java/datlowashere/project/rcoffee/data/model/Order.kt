package datlowashere.project.rcoffee.data.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Order(
    val order_id: String,
    val order_date: Date? = null,
    val create_at: Date? = null,
    val update_at: Date? = null,
    val payment_method: String?,
    val total_amount: Double,
    val discount_amount: Double = 0.0,
    val status_order: String = " ",
    val address_id: Int?,
    val voucher_id: Int?,
    val order_message: String,
    val basket_id: List<Int>,
    val quantity: Int = 0,
    val total_item: Int = 0,
    val name: String = "",
    val img: String = "",
    val price: Double = 0.0,
    val location: String = "",
    val payment_status: String = "",
    val payment_id: Int = 0,
    val reason: String = "",
    val email_user: String =" ",
    val token:String=" "
) : Parcelable {
    constructor(order_id: String, status_order: String) : this(
        order_id = order_id,
        status_order = status_order,
        order_date = null,
        create_at = null,
        update_at = null,
        payment_method = null,
        total_amount = 0.0,
        discount_amount = 0.0,
        address_id = null,
        voucher_id = null,
        order_message = "",
        basket_id = emptyList(),
        quantity = 0,
        total_item = 0,
        name = "",
        img = "",
        price = 0.0,
        location = "",
        payment_status = "",
        payment_id = 0,
        reason = "",
        email_user=" ",
        token = " "
    )
    constructor(parcel: Parcel) : this(
        order_id = parcel.readString() ?: "",
        order_date = parcel.readSerializable() as? Date,
        create_at = parcel.readSerializable() as? Date,
        update_at = parcel.readSerializable() as? Date,
        payment_method = parcel.readString(),
        total_amount = parcel.readDouble(),
        discount_amount = parcel.readDouble(),
        status_order = parcel.readString() ?: " ",
        address_id = parcel.readValue(Int::class.java.classLoader) as? Int,
        voucher_id = parcel.readValue(Int::class.java.classLoader) as? Int,
        order_message = parcel.readString() ?: "",
        basket_id = mutableListOf<Int>().apply { parcel.readList(this, Int::class.java.classLoader) },
        quantity = parcel.readInt(),
        total_item = parcel.readInt(),
        name = parcel.readString() ?: "",
        img = parcel.readString() ?: "",
        price = parcel.readDouble(),
        location = parcel.readString() ?: "",
        payment_status = parcel.readString() ?: "",
        payment_id = parcel.readInt(),
        reason = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(order_id)
        parcel.writeSerializable(order_date)
        parcel.writeSerializable(create_at)
        parcel.writeSerializable(update_at)
        parcel.writeString(payment_method)
        parcel.writeDouble(total_amount)
        parcel.writeDouble(discount_amount)
        parcel.writeString(status_order)
        parcel.writeValue(address_id)
        parcel.writeValue(voucher_id)
        parcel.writeString(order_message)
        parcel.writeList(basket_id)
        parcel.writeInt(quantity)
        parcel.writeInt(total_item)
        parcel.writeString(name)
        parcel.writeString(img)
        parcel.writeDouble(price)
        parcel.writeString(location)
        parcel.writeString(payment_status)
        parcel.writeInt(payment_id)
        parcel.writeString(reason)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
