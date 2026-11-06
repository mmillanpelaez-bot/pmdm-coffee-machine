package org.example

import org.example.CoffeeMachineState.*

/**
 * Cafetera. Es un singleton (`object`): solo hay una en toda la aplicación.
 */
object CoffeeMachine {

    /** Estado actual: se puede leer desde fuera, pero solo la máquina lo cambia. */
    var currentState: CoffeeMachineState = Idle
        private set

    /**
     * Realiza como máximo una transición: E1 -> E2 -> Elaborando -> E3 -> E1 (o E0 si hay errores).
     *
     * @param monedero saldo disponible
     * @param seleccion bebida elegida (null si no ha elegido)
     * @param hayErrores simula un fallo al terminar la bebida
     * @return el estado en el que queda la máquina
     */
    fun gestorEstados(
        monedero: Int,
        seleccion: Bebida? = null,
        hayErrores: Boolean = false
    ): CoffeeMachineState {
        println("Estado actual: $currentState")

        when (val estado = currentState) {
            // E1 -> E2 (o sigue esperando)
            is Idle -> {
                if (seleccion != null) {
                    currentState = ProcessingSelection(seleccion)
                    println("Selección recibida: $seleccion")
                } else {
                    println("Sigo esperando una selección")
                }
            }
            // E2 -> Elaborando (o muestra el precio si no hay saldo)
            is ProcessingSelection -> {
                if (monedero >= estado.seleccion.precio) {
                    currentState = when (estado.seleccion) {
                        Bebida.CORTADO -> MakingCoffee.Cortado
                        Bebida.LARGO -> MakingCoffee.Largo
                        Bebida.CAPPUCCINO -> MakingCoffee.Cappuccino
                    }
                    println("Saldo suficiente")
                } else {
                    println("Saldo insuficiente. ${estado.seleccion} cuesta ${estado.seleccion.precio}")
                }
            }
            // Elaborando -> E3
            is MakingCoffee -> {
                println("¡Estoy haciendo ${estado.bebida}! Por favor, espera...")
                Thread.sleep(2000) // simula la preparación
                currentState = Finished(estado.bebida)
            }
            // E3 -> E1 (o E0 si hay errores)
            is Finished -> {
                if (hayErrores) {
                    currentState = Error("Fallo al terminar ${estado.bebida}", origen = estado)
                } else {
                    println("Ya tienes tu ${estado.bebida} en ${estado.taza}. Por favor, tómalo.")
                    currentState = Idle // resetea la máquina
                }
            }
            // E0: solo se sale con clean()
            is Error -> println("La máquina tiene un error: ${estado.message}")
        }
        return currentState
    }

    /** Limpia la máquina y la resetea a [Idle] desde cualquier estado. */
    fun clean() {
        println("Limpiando la máquina...")
        currentState = Idle
        println("Máquina limpia. Estado: $currentState")
    }
}
