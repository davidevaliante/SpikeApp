package com.musashi.claymore.spike.spike.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.musashi.claymore.spike.spike.constants.DetailStatus

class DetailActivityViewModel : ViewModel(){
    var state : MutableLiveData<DetailStatus> = MutableLiveData()
    fun updateState(newValue:DetailStatus)=state.postValue(newValue)
}