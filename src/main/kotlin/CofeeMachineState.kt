package org.example

/**
 * Bebidas que sabe preparar la máquina (nodos con * del diagrama) y su precio.
 */
enum class Bebida(val precio: Int) {
    CORTADO(1),
    LARGO(1),
    CAPPUCCINO(2)   // con leche, azúcar, espuma...
}

sealed class CoffeeMachineState {

    /** E0 - ERROR (por estado): guarda el estado en el que se produjo y el motivo */
    data class Error(val message: String, val origen: CoffeeMachineState? = null) : CoffeeMachineState()

    /** E1 - Esperando una selección */
    data class Idle(val timestamp: Long = System.currentTimeMillis()) : CoffeeMachineState()

    /**
     * E2 - Procesando selección: comprueba que el saldo sea suficiente,
     * si no, muestra el precio de la selección.
     */
    data class ProcessingSelection(val seleccion: Bebida) : CoffeeMachineState()

    /** Elaborando: un estado por cada bebida del diagrama */
    sealed class MakingCoffee(val bebida: Bebida) : CoffeeMachineState() {
        object Cortado : MakingCoffee(Bebida.CORTADO)
        object Largo : MakingCoffee(Bebida.LARGO)
        object Cappuccino : MakingCoffee(Bebida.CAPPUCCINO)
    }

    /** E3 - Terminado: comprueba que no haya errores antes de volver a E1 */
    data class Finished(val bebida: Bebida) : CoffeeMachineState()
}
