package org.example

import org.example.CoffeeMachineState.*

/**
 * Cafetera. Es un **singleton** (`object`): solo puede haber una en toda la aplicación.
 * Hereda de [Appliance] para reutilizar su `log`.
 *
 * El estado actual es privado: desde fuera solo se lee con [state] y se cambia
 * llamando a [manageState] o [clean].
 */
object CoffeeMachine : Appliance("Cafetera") {

    private var currentState: CoffeeMachineState = Idle
    private val changes = mutableListOf<StateChange>()

    /** Estado actual (solo lectura). */
    val state: CoffeeMachineState get() = currentState

    /** Historial de cambios de estado (copia de solo lectura). */
    val history: List<StateChange> get() = changes.toList()

    /** Redefine el `log` de [Appliance] para añadirle un icono. */
    override fun log(message: String) = super.log("☕ $message")

    /**
     * Gestor de estados: realiza **como máximo una transición** por llamada,
     * siguiendo el esquema (E1 -> E2 -> Elaborando -> E3 -> E1, o E0 si hay errores).
     *
     * @param wallet saldo disponible.
     * @param selection bebida elegida por el usuario (`null` si no ha elegido).
     * @param hasErrors simula un fallo detectado al terminar la bebida.
     * @return el estado en el que queda la máquina.
     */
    fun manageState(wallet: Int, selection: Drink? = null, hasErrors: Boolean = false): CoffeeMachineState {
        log("Estado actual: $currentState")
        when (val current = currentState) {
            // E1 -> E2 (o se queda esperando)
            is Idle -> if (selection != null) {
                changeState(ProcessingSelection(selection))
            } else {
                log("Sigo esperando una selección...")
            }
            // E2 -> Elaborando (o se queda mostrando el precio)
            is ProcessingSelection ->
                if (wallet >= current.selection.price) {
                    changeState(startMaking(current.selection))
                } else {
                    log("Saldo insuficiente. ${current.selection} cuesta ${current.selection.price}.")
                }
            // Elaborando -> E3
            is MakingCoffee -> {
                log("Estoy haciendo ${current.drink}. Por favor, espera...")
                Thread.sleep(1000) // simula la elaboración
                changeState(Finished(current.drink))
            }
            // E3 -> E1 (o E0 si hay errores)
            is Finished ->
                if (hasErrors) {
                    changeState(Error("Fallo al terminar ${current.drink}", origin = current))
                } else {
                    log("Ya tienes tu ${current.drink} en ${current.cup}. ¡Que aproveche!")
                    changeState(Idle)
                }
            // E0: solo se sale con clean()
            is Error -> log("La máquina tiene un error: ${current.message}. Hay que limpiarla.")
        }
        return currentState
    }

    /** Limpia la máquina y la **resetea** a [Idle] desde cualquier estado, incluido [Error]. */
    fun clean() {
        log("Limpiando la máquina...")
        changeState(Idle)
        log("Máquina limpia. Estado: $currentState")
    }

    /** Imprime el historial de estados por los que ha pasado la máquina. */
    fun printHistory() {
        log("Historial (${changes.size} cambios):")
        changes.forEachIndexed { i, c -> println("   ${i + 1}. ${c.from} -> ${c.to}") }
    }

    /** Devuelve el estado de elaboración que corresponde a la bebida. `when` exhaustivo sobre el `enum`. */
    private fun startMaking(drink: Drink): MakingCoffee = when (drink) {
        Drink.CORTADO -> MakingCoffee.Cortado
        Drink.LARGO -> MakingCoffee.Largo
        Drink.CAPPUCCINO -> MakingCoffee.Cappuccino
    }

    /** Único punto donde cambia el estado: así el historial nunca se queda sin registrar un cambio. */
    private fun changeState(new: CoffeeMachineState) {
        changes += StateChange(from = currentState, to = new)
        currentState = new
        log("Nuevo estado: $new")
    }
}
