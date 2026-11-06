package org.example

/**
 * Bebidas que sabe preparar la máquina (nodos con * del diagrama), su precio y su taza.
 *
 * @property precio precio de la bebida.
 * @property taza taza en la que se sirve (cada tipo de taza es una subclase de [Taza]).
 */
enum class Bebida(val precio: Int, val taza: Taza) {
    CORTADO(1, TazaEspresso()),
    LARGO(1, TazaGrande()),
    CAPPUCCINO(2, TazaCappuccino())   // con leche, azúcar, espuma...
}

/**
 * Estados de la cafetera, según el esquema hecho en papel (5 estados).
 *
 * Es una **clase sellada** (`sealed class`): todas las subclases están en este fichero,
 * así que el compilador sabe cuáles son y obliga a que cualquier `when` las cubra todas.
 *
 * Criterio para elegir el tipo de cada estado:
 * - `object` -> estado **sin datos**: solo existe una instancia (singleton).
 * - `data class` -> estado **con datos**: cada instancia guarda información distinta
 *   y el compilador genera `equals()`, `hashCode()`, `toString()` y `copy()`.
 */
sealed class CoffeeMachineState {

    /** E0 - ERROR (por estado): guarda el estado en el que se produjo y el motivo */
    data class Error(val message: String, val origen: CoffeeMachineState? = null) : CoffeeMachineState()

    /** E1 - Esperando una selección. Guarda desde cuándo espera, por eso es una `data class`. */
    data class Idle(val timestamp: Long = System.currentTimeMillis()) : CoffeeMachineState()

    /**
     * E2 - Procesando selección: comprueba que el saldo sea suficiente,
     * si no, muestra el precio de la selección.
     *
     * **Estado con datos que no estaba en el ejemplo de la práctica.**
     */
    data class ProcessingSelection(val seleccion: Bebida) : CoffeeMachineState()

    /**
     * Elaborando: un estado por cada bebida del diagrama.
     * Cada bebida es un `object` porque no necesita datos propios (precio y taza están en [Bebida]).
     */
    sealed class MakingCoffee(val bebida: Bebida) : CoffeeMachineState() {
        object Cortado : MakingCoffee(Bebida.CORTADO) { override fun toString() = "Cortado" }
        object Largo : MakingCoffee(Bebida.LARGO) { override fun toString() = "Largo" }
        object Cappuccino : MakingCoffee(Bebida.CAPPUCCINO) { override fun toString() = "Cappuccino" }
    }

    /**
     * E3 - Terminado: comprueba que no haya errores antes de volver a E1.
     *
     * @property bebida bebida servida.
     * @property taza taza en la que se ha servido.
     */
    data class Finished(val bebida: Bebida, val taza: Taza = bebida.taza) : CoffeeMachineState()
}

/**
 * Registro de un cambio de estado. Es una `data class` porque solo agrupa datos;
 * se usa para guardar el **historial de estados** de la máquina.
 *
 * @property from estado anterior.
 * @property to estado nuevo.
 * @property timestamp momento del cambio (milisegundos desde 1970).
 */
data class StateChange(
    val from: CoffeeMachineState,
    val to: CoffeeMachineState,
    val timestamp: Long = System.currentTimeMillis()
)
