/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Clase que se corresponde con la tabla guitarra de la base de datos.
 */


package com.farkuzio58.guitarcatalog.data

data class Guitar(
    val id: Int,
    val idExterno: String,
    val marca: String,
    val modelo: String,
    val forma: String,
    val madera_cuerpo: String,
    val madera_mastil: String,
    val madera_diapason: String,
    val tapa: String,
    val configuracion: String,
    val pastillas: String,
    val trastes: String,
    val nCuerdas: String,
    val tremolo: Boolean,
    val precio: Float,
    val urlImagen: String,
    val url: String,
    val isActive: Boolean,
    val rotacion: Float
) {
}