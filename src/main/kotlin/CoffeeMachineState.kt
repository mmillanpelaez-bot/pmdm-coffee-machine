package org.example

/**
 * Bebidas que sabe preparar la máquina (nodos con * del esquema), su precio y su taza.
 *
 * @property price precio de la bebida.
 * @property cup taza en la que se sirve (cada tipo de taza es una subclase de [Cup]).
 */
enum class Drink(val price: Int, val cup: Cup) {
    CORTADO(1, EspressoCup()),
    LARGO(1, LargeMug()),
    CAPPUCCINO(2, CappuccinoCup()) // con leche, azúcar, espuma...
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

    /** E0 - ERROR (por estado). Guarda el motivo y el estado en el que se produjo. */
    data class Error(val message: String, val origin: CoffeeMachineState? = null) : CoffeeMachineState()

    /** E1 - Esperando una selección. Sin datos, por eso es un `object`. */
    object Idle : CoffeeMachineState() {
        override fun toString() = "Idle"
    }

    /**
     * E2 - Procesando selección: comprueba que el saldo sea suficiente
     * y, si no, muestra el precio de la selección.
     *
     * **Estado con datos que no estaba en el ejemplo de la práctica.**
     *
     * @property selection bebida elegida por el usuario.
     */
    data class ProcessingSelection(val selection: Drink) : CoffeeMachineState()

    /**
     * Elaborando. Es a su vez una clase sellada con un estado por cada bebida del esquema.
     * Cada bebida es un `object` porque no necesita datos propios (la información está en [Drink]).
     *
     * @property drink bebida que se está elaborando.
     */
    sealed class MakingCoffee(val drink: Drink) : CoffeeMachineState() {
        object Cortado : MakingCoffee(Drink.CORTADO) { override fun toString() = "Cortado" }
        object Largo : MakingCoffee(Drink.LARGO) { override fun toString() = "Largo" }
        object Cappuccino : MakingCoffee(Drink.CAPPUCCINO) { override fun toString() = "Cappuccino" }
    }

    /**
     * E3 - Terminado: antes de volver a E1 se comprueba que no haya errores.
     *
     * @property drink bebida servida.
     * @property cup taza en la que se ha servido.
     */
    data class Finished(val drink: Drink, val cup: Cup = drink.cup) : CoffeeMachineState()
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
