package org.example

import org.example.CoffeeMachineState.*

/**
 * Singleton que maneja lo que ocurre en cada estado
 */
object CoffeeMachine {
    public var currentState: CoffeeMachineState = CoffeeMachineState.Idle()

    /**
     * Realiza como máximo una transición.
     * @param monedero saldo disponible
     * @param seleccion bebida elegida por el usuario (null si no ha elegido)
     * @param hayErrores simula un fallo detectado al terminar
     */
    fun gestorEstados(
        monedero: Int,
        seleccion: Bebida? = null,
        hayErrores: Boolean = false
    ): CoffeeMachineState {
        println("Estado actual: $currentState")

        val estado = currentState
        when (estado) {
            // E1 -> E2
            is Idle -> {
                if (seleccion != null) {
                    currentState = ProcessingSelection(seleccion)
                    println("Selección recibida: $seleccion. Estado: $currentState")
                } else {
                    println("Sigo en el estado IDLE")
                }
            }
            // E2 -> Elaborando (o se queda mostrando el precio)
            is ProcessingSelection -> {
                if (monedero >= estado.seleccion.precio) {
                    currentState = when (estado.seleccion) {
                        Bebida.CORTADO -> MakingCoffee.Cortado
                        Bebida.LARGO -> MakingCoffee.Largo
                        Bebida.CAPPUCCINO -> MakingCoffee.Cappuccino
                    }
                    println("Saldo suficiente. Estado: $currentState")
                } else {
                    println("Saldo insuficiente. ${estado.seleccion} cuesta ${estado.seleccion.precio}")
                }
            }
            // Elaborando -> E3
            is MakingCoffee -> {
                println("¡Estoy haciendo ${estado.bebida}! Por favor, espera...")
                Thread.sleep(2000)
                currentState = Finished(estado.bebida)
            }
            // E3 -> E1 (o E0 si hay errores)
            is Finished -> {
                if (hayErrores) {
                    currentState = Error("Fallo al terminar ${estado.bebida}", origen = estado)
                } else {
                    println("Ya tienes tu ${estado.bebida}. Por favor, tómalo.")
                    currentState = Idle()
                }
            }
            // E0
            is Error -> {
                println("La máquina tiene un error: ${estado.message}")
            }
        }
        return currentState
    }

    fun clean() {
        println("Limpiando la máquina...")
        currentState = CoffeeMachineState.Idle()
        println("Máquina limpia. Estado: $currentState")
    }
}
