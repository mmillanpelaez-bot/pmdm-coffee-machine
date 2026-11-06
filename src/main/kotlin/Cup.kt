package org.example

/**
 * Taza genérica en la que se sirve la bebida.
 *
 * Es una clase **`open`** para poder heredar de ella: cada tipo de taza
 * redefine (`override`) la descripción y aporta su propia capacidad.
 *
 * @property name nombre del tipo de taza.
 * @property capacityMl capacidad en mililitros.
 */
open class Cup(val name: String, val capacityMl: Int) {

    /** Descripción legible de la taza. Se puede redefinir en las subclases. */
    open fun description(): String = "$name de $capacityMl ml"

    override fun toString(): String = description()
}

/** Taza pequeña para el cortado. Hereda de [Cup] y extiende su descripción. */
class EspressoCup : Cup(name = "Taza de espresso", capacityMl = 60) {
    override fun description(): String = super.description() + " (pequeña y precalentada)"
}

/** Taza para el café largo. Hereda de [Cup] y extiende su descripción. */
class LargeMug : Cup(name = "Taza grande", capacityMl = 300) {
    override fun description(): String = super.description() + " (con asa)"
}

/** Taza para el cappuccino. Hereda de [Cup] y extiende su descripción. */
class CappuccinoCup : Cup(name = "Taza de cappuccino", capacityMl = 250) {
    override fun description(): String = super.description() + " (ancha, para la espuma)"
}
