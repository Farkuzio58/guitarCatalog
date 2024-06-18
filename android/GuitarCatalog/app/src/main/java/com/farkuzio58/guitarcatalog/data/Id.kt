/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Clase id que almacenará en la base de datos interna del móvil el id de sus guitarras favoritas.
 */

package com.farkuzio58.guitarcatalog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "id")
data class Id(@PrimaryKey val guitarId: Int) {
}