package org.example

/**
 * Taza genérica en la que se sirve la bebida.
 *
 * Es una clase **`open`** para poder heredar de ella: cada tipo de taza
 * redefine (`override`) la descripción y aporta su propia capacidad.
 *
 * @property nombre nombre del tipo de taza.
 * @property capacidadMl capacidad en mililitros.
 */
open class Taza(val nombre: String, val capacidadMl: Int) {

    /** Descripción legible de la taza. Se puede redefinir en las subclases. */
    open fun descripcion(): String = "$nombre de $capacidadMl ml"

    override fun toString(): String = descripcion()
}

/** Taza pequeña para el cortado. Hereda de [Taza] y extiende su descripción. */
class TazaEspresso : Taza(nombre = "Taza de espresso", capacidadMl = 60) {
    override fun descripcion(): String = super.descripcion() + " (pequeña y precalentada)"
}

/** Taza para el café largo. Hereda de [Taza] y extiende su descripción. */
class TazaGrande : Taza(nombre = "Taza grande", capacidadMl = 300) {
    override fun descripcion(): String = super.descripcion() + " (con asa)"
}

/** Taza para el cappuccino. Hereda de [Taza] y extiende su descripción. */
class TazaCappuccino : Taza(nombre = "Taza de cappuccino", capacidadMl = 250) {
    override fun descripcion(): String = super.descripcion() + " (ancha, para la espuma)"
}
