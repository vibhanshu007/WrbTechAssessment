package com.vibhanshu.wrbtechassessment.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object Weather : Screen()
    @Serializable
    data object Search : Screen()
}
