package com.example.fypfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/*
Allows Fragments to be shared between each other
In this context its the hr_button from HomeFragment
 */
//class SharedViewModel : ViewModel() {
//    private val _isHrButtonVisible = MutableLiveData(true)
//    val isHrButtonVisible: LiveData<Boolean> = _isHrButtonVisible
//
//
//    fun setHrButtonVisibility(isVisible: Boolean) {
//        _isHrButtonVisible.value = isVisible
//    }
//}

//class SharedViewModel : ViewModel() {
//    private val _isHrButtonVisible = MutableLiveData(true)
//    val isHrButtonVisible: LiveData<Boolean>
//        get() = _isHrButtonVisible
//
//    fun setHrButtonVisibility(isVisible: Boolean) {
//        _isHrButtonVisible.value = isVisible
//    }
//}

class SharedViewModel : ViewModel() {

    private val _isHrButtonVisible = MutableLiveData(true)
    val isHrButtonVisible: LiveData<Boolean> = _isHrButtonVisible

    fun updateHrButtonVisibility(isVisible: Boolean) {
        _isHrButtonVisible.postValue(isVisible)
    }
}


