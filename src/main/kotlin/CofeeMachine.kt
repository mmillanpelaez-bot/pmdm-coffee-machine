package org.example

import org.example.CoffeeMachineState.*

/**
 * Cafetera. Es un **singleton** (`object`): solo puede haber una en toda la aplicación.
 * Hereda de [Electrodomestico] para reutilizar su `log`.
 *
 * El estado actual se puede leer desde fuera ([currentState]) pero solo cambia
 * mediante [gestorEstados] y [clean].
 */
object CoffeeMachine : Electrodomestico("Cafetera") {

    /** Estado actual: público para leerlo, pero con `setter` privado. */
    var currentState: CoffeeMachineState = Idle()
        private set

    private val cambios = mutableListOf<StateChange>()

    /** Historial de cambios de estado (copia de solo lectura). */
    val history: List<StateChange> get() = cambios.toList()

    /** Redefine el `log` de [Electrodomestico] para añadirle un icono. */
    override fun log(mensaje: String) = super.log("☕ $mensaje")

    /**
     * Realiza como máximo una transición:
     * E1 -> E2 -> Elaborando -> E3 -> E1 (o E0 si hay errores).
     *
     * @param monedero saldo disponible
     * @param seleccion bebida elegida por el usuario (null si no ha elegido)
     * @param hayErrores simula un fallo detectado al terminar
     * @return el estado en el que queda la máquina
     */
    fun gestorEstados(
        monedero: Int,
        seleccion: Bebida? = null,
        hayErrores: Boolean = false
    ): CoffeeMachineState {
        log("Estado actual: $currentState")

        when (val estado = currentState) {
            // E1 -> E2
            is Idle -> {
                if (seleccion != null) {
                    changeState(ProcessingSelection(seleccion))
                    log("Selección recibida: $seleccion.")
                } else {
                    log("Sigo en el estado IDLE")
                }
            }
            // E2 -> Elaborando (o se queda mostrando el precio)
            is ProcessingSelection -> {
                if (monedero >= estado.seleccion.precio) {
                    changeState(elaborar(estado.seleccion))
                    log("Saldo suficiente.")
                } else {
                    log("Saldo insuficiente. ${estado.seleccion} cuesta ${estado.seleccion.precio}")
                }
            }
            // Elaborando -> E3
            is MakingCoffee -> {
                log("¡Estoy haciendo ${estado.bebida}! Por favor, espera...")
                Thread.sleep(2000)
                changeState(Finished(estado.bebida))
            }
            // E3 -> E1 (o E0 si hay errores)
            is Finished -> {
                if (hayErrores) {
                    changeState(Error("Fallo al terminar ${estado.bebida}", origen = estado))
                } else {
                    log("Ya tienes tu ${estado.bebida} en ${estado.taza}. Por favor, tómalo.")
                    changeState(Idle())
                }
            }
            // E0: solo se sale con clean()
            is Error -> log("La máquina tiene un error: ${estado.message}")
        }
        return currentState
    }

    /** Limpia la máquina y la **resetea** a [Idle] desde cualquier estado, incluido [Error]. */
    fun clean() {
        log("Limpiando la máquina...")
        changeState(Idle())
        log("Máquina limpia. Estado: $currentState")
    }

    /** Imprime el historial de estados por los que ha pasado la máquina. */
    fun printHistory() {
        log("Historial (${cambios.size} cambios):")
        cambios.forEachIndexed { i, c -> println("   ${i + 1}. ${c.from} -> ${c.to}") }
    }

    /** Devuelve el estado de elaboración que corresponde a la bebida (`when` exhaustivo sobre el `enum`). */
    private fun elaborar(bebida: Bebida): MakingCoffee = when (bebida) {
        Bebida.CORTADO -> MakingCoffee.Cortado
        Bebida.LARGO -> MakingCoffee.Largo
        Bebida.CAPPUCCINO -> MakingCoffee.Cappuccino
    }

    /** Único punto donde cambia el estado: así el historial nunca se queda sin registrar un cambio. */
    private fun changeState(nuevo: CoffeeMachineState) {
        cambios += StateChange(from = currentState, to = nuevo)
        currentState = nuevo
    }
}
