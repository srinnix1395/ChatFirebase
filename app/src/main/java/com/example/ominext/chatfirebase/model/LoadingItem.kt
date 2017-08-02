package com.example.ominext.chatfirebase.model

/**
 * Created by Ominext on 8/2/2017.
 */


class LoadingItem {

    var loadingState = STATE.LOADING
}

enum class STATE{
    LOADING,
    ERROR
}
