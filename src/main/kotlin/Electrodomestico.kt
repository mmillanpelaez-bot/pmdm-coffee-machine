package org.example

/**
 * Clase base de cualquier electrodoméstico.
 *
 * Es `open` para que otras clases (o `object`s, como [CoffeeMachine]) puedan heredar
 * de ella y reutilizar la forma de escribir mensajes por consola.
 *
 * @property nombre nombre que aparece delante de cada mensaje.
 */
open class Electrodomestico(val nombre: String) {

    /** Escribe un mensaje por consola. `protected open`: solo visible para las subclases, que pueden redefinirlo. */
    protected open fun log(mensaje: String) {
        println("[$nombre] $mensaje")
    }
}
