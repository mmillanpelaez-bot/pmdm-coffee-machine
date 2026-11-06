package org.example

/**
 * Taza en la que se sirve la bebida.
 * Es `open` para que otras clases puedan heredar de ella.
 *
 * @property nombre nombre de la taza.
 */
open class Taza(val nombre: String) {
    /** Descripción de la taza. Es `open` para poder redefinirla en las subclases. */
    open fun descripcion(): String = nombre

    override fun toString(): String = descripcion()
}

/** Taza pequeña (hereda de [Taza]). */
class TazaPequena : Taza("Taza pequeña") {
    override fun descripcion(): String = super.descripcion() + " de 60 ml"
}

/** Taza grande (hereda de [Taza]). */
class TazaGrande : Taza("Taza grande") {
    override fun descripcion(): String = super.descripcion() + " de 300 ml"
}
