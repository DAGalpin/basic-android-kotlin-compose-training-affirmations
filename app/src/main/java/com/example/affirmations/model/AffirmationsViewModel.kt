package com.example.affirmations.model

import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.affirmations.data.Datasource

data class Affirmation(
    @StringRes val stringResourceId: Int,
    val imageResourceURI: String
)

class AffirmationsViewModel : ViewModel() {
    private val _affirmations = mutableStateListOf<Affirmation>()
    val affirmations: List<Affirmation> = _affirmations
    init {
        val affirmationList = Datasource().loadAffirmations()
        _affirmations.addAll(affirmationList)
    }
    fun setAffirmationURI(index: Int, uri: String) {
        _affirmations[index] = _affirmations[index].copy(imageResourceURI = uri)
    }
}