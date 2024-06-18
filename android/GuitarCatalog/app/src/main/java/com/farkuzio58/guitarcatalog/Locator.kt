/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Objeto Locator para inniciar la aplicación.
 */

package com.farkuzio58.guitarcatalog

import android.app.Application

object Locator {
    private var application: Application? = null
    val requireApplication: Application get() = application ?: error("Aplicacion no iniciada")

    fun initWith(app: Application){
        application = app
    }
}