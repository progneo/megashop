package me.progneo.megashop.data.exception

internal class RequestException(val code: Int, message: String) : Throwable(message)
