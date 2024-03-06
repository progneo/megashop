package me.progneo.megashop.data.exception

import java.io.IOException

internal class NoConnectivityException : IOException() {

    override val message: String
        get() = "No Internet Connection"
}
