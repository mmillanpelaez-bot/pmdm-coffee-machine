package org.example

/** Recorre el esquema completo: camino normal, saldo insuficiente y camino de error. */
fun main() {
    println("--- Sin selección: sigue en Idle ---")
    CoffeeMachine.gestorEstados(monedero = 2)

    println("\n--- Cappuccino con saldo insuficiente ---")
    CoffeeMachine.gestorEstados(monedero = 1, seleccion = Bebida.CAPPUCCINO) // Idle -> ProcessingSelection
    CoffeeMachine.gestorEstados(monedero = 1)                                // muestra el precio

    println("\n--- Ahora con saldo suficiente ---")
    CoffeeMachine.gestorEstados(monedero = 2) // ProcessingSelection -> Cappuccino
    CoffeeMachine.gestorEstados(monedero = 2) // Cappuccino -> Finished
    CoffeeMachine.gestorEstados(monedero = 2) // Finished -> Idle

    println("\n--- Cortado que termina con error ---")
    CoffeeMachine.gestorEstados(monedero = 1, seleccion = Bebida.CORTADO)
    CoffeeMachine.gestorEstados(monedero = 1)
    CoffeeMachine.gestorEstados(monedero = 1)
    CoffeeMachine.gestorEstados(monedero = 1, hayErrores = true) // Finished -> Error
    CoffeeMachine.gestorEstados(monedero = 1)                    // sigue en Error

    println("\n--- Limpiando la máquina (sale del error) ---")
    CoffeeMachine.clean()

    println("\n--- Historial ---")
    CoffeeMachine.printHistory()
}
