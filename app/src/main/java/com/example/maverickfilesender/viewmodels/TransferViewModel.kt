package com.example.maverickfilesender.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class TransferViewModel(application: Application):AndroidViewModel(application) {

    var mutableLiveData:MutableLiveData<Int>?=null
    var isFileTransferring:MutableLiveData<Boolean>?=null

var fileSize:Int=0
init{
    mutableLiveData=MutableLiveData<Int>()
    isFileTransferring=MutableLiveData<Boolean>()
}

}