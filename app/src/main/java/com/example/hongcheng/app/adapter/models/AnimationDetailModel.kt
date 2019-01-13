package com.example.hongcheng.app.adapter.models

import android.databinding.BaseObservable
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by hongcheng on 17/6/4.
 */
class AnimationDetailModel() : BaseObservable(), Parcelable {
    var imageUrl: String? = null

    var content: String? = null

    var description: String? = null

    var date: String? = null

    var infoId: String? = null

    var type: String? = null
    var cardType: String? = null

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<AnimationDetailModel> = object : Parcelable.Creator<AnimationDetailModel> {
            override fun createFromParcel(source: Parcel): AnimationDetailModel = AnimationDetailModel(source)
            override fun newArray(size: Int): Array<AnimationDetailModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this()
    {
        this.imageUrl = source.readString()
        this.content = source.readString()
        this.description = source.readString()
        this.date = source.readString()
        this.infoId = source.readString()
        this.type = source.readString()
        this.cardType = source.readString()
    }

    constructor(imageUrl: String?, content: String?, description: String?, date: String?, infoId: String?) : this() {
        this.imageUrl = imageUrl
        this.content = content
        this.description = description
        this.date = date
        this.infoId = infoId
    }

    constructor(imageUrl: String?, content: String?, description: String?, date: String?, infoId: String?, type: String?, cardType: String?) : this() {
        this.imageUrl = imageUrl
        this.content = content
        this.description = description
        this.date = date
        this.infoId = infoId
        this.type = type
        this.cardType = cardType
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(imageUrl)
        dest.writeString(content)
        dest.writeString(description)
        dest.writeString(date)
        dest.writeString(infoId)
        dest.writeString(type)
        dest.writeString(cardType)
    }
}