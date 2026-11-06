package org.example

/**
 * Clase base de cualquier electrodoméstico.
 *
 * Es `open` para que otras clases (o `object`s, como [CoffeeMachine]) puedan heredar
 * de ella y reutilizar la forma de escribir mensajes por consola.
 *
 * @property name nombre que aparece delante de cada mensaje.
 */
open class Appliance(val name: String) {

    /** Escribe un mensaje por consola. `protected open`: solo visible para las subclases, que pueden redefinirlo. */
    protected open fun log(message: String) {
        println("[$name] $message")
    }
}
