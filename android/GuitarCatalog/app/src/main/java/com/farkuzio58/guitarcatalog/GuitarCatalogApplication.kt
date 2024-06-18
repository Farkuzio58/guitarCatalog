/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Clase que hereda de Application.
 */

package com.farkuzio58.guitarcatalog

import android.app.Application
import com.farkuzio58.guitarcatalog.Locator.initWith

class GuitarCatalogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initWith(this)
    }

}