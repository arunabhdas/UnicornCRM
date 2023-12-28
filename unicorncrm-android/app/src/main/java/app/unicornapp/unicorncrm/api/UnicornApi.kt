package app.unicornapp.unicorncrm.api

import retrofit2.http.GET

interface UnicornApi {
    @GET("/unicorn/api/")
    fun callApi() {

    }
}