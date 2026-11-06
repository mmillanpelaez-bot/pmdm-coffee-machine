package org.example

/**
 * Bebidas de la máquina (nodos con * del esquema), con su precio y su taza.
 */
enum class Bebida(val precio: Int, val taza: Taza) {
    CORTADO(1, TazaPequena()),
    LARGO(1, TazaGrande()),
    CAPPUCCINO(2, TazaGrande())   // con leche, azúcar, espuma...
}

/**
 * Estados de la cafetera (5, según el esquema en papel).
 *
 * Es una clase sellada: el compilador conoce todos los estados y obliga a que
 * cada `when` los cubra todos.
 *
 * - `object`: estado sin datos (una única instancia).
 * - `data class`: estado que guarda información.
 */
sealed class CoffeeMachineState {

    /** E0 - Error. Guarda el motivo y el estado donde ocurrió (por eso es `data class`). */
    data class Error(val message: String, val origen: CoffeeMachineState? = null) : CoffeeMachineState()

    /** E1 - Esperando. No necesita datos (por eso es `object`). */
    object Idle : CoffeeMachineState() {
        override fun toString() = "Idle"
    }

    /**
     * E2 - Procesando selección: comprueba el saldo y, si no llega, muestra el precio.
     * Estado nuevo con datos (la bebida elegida), por eso es `data class`.
     */
    data class ProcessingSelection(val seleccion: Bebida) : CoffeeMachineState()

    /** Elaborando: un estado por bebida. Son `object` porque no guardan datos propios. */
    sealed class MakingCoffee(val bebida: Bebida) : CoffeeMachineState() {
        object Cortado : MakingCoffee(Bebida.CORTADO) { override fun toString() = "Cortado" }
        object Largo : MakingCoffee(Bebida.LARGO) { override fun toString() = "Largo" }
        object Cappuccino : MakingCoffee(Bebida.CAPPUCCINO) { override fun toString() = "Cappuccino" }
    }

    /** E3 - Terminado. Guarda la bebida y la taza (por eso es `data class`). */
    data class Finished(val bebida: Bebida, val taza: Taza = bebida.taza) : CoffeeMachineState()
}
