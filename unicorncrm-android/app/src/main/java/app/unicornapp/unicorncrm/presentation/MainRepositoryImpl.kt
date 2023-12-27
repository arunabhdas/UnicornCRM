package app.unicornapp.unicorncrm.presentation

import app.unicornapp.unicorncrm.api.UnicornApi

class MainRepositoryImpl(
    private val api: UnicornApi
) : MainRepository{
    override fun doNetworkCall() {
        TODO("Not yet implemented")
    }
}