package com.example.a35b_crud.model

import android.os.Parcel
import android.os.Parcelable

data class CartModel(
    var cartId: String = "",
    var productId: String = "",
    var productName: String = "",
    var productDesc: String = "",
    var price: Int = 0,
    var imageUrl: String = "",
    var quantity: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cartId)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productDesc)
        parcel.writeInt(price)
        parcel.writeString(imageUrl)
        parcel.writeInt(quantity)
    }
    override fun describeContents(): Int = 0
    companion object CREATOR : Parcelable.Creator<CartModel> {
        override fun createFromParcel(parcel: Parcel): CartModel = CartModel(parcel)
        override fun newArray(size: Int): Array<CartModel?> = arrayOfNulls(size)
    }
}
