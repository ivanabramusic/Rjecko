package com.example.rjecko.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatsModel(
    @PropertyName("guess6") val guess6: Int = 0,
    @PropertyName("guess5") val guess5: Int = 0,
    @PropertyName("guess4") val guess4: Int = 0,
    @PropertyName("guess3") val guess3: Int = 0,
    @PropertyName("guess2") val guess2: Int = 0,
    @PropertyName("guess1") val guess1: Int = 0,
    @PropertyName("noguess") val noguess: Int = 0
) : Parcelable {
    constructor() : this(0, 0, 0, 0, 0, 0, 0) // No-argument constructor
}
