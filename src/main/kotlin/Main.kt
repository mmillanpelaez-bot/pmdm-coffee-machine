package org.example

/** Recorre el esquema completo: camino normal, saldo insuficiente y camino de error. */
fun main() {
    println("--- Sin selección: sigue en Idle ---")
    CoffeeMachine.manageState(wallet = 2)

    println("\n--- Cappuccino con saldo insuficiente ---")
    CoffeeMachine.manageState(wallet = 1, selection = Drink.CAPPUCCINO) // Idle -> ProcessingSelection
    CoffeeMachine.manageState(wallet = 1)                               // muestra el precio

    println("\n--- Ahora con saldo suficiente ---")
    CoffeeMachine.manageState(wallet = 2) // ProcessingSelection -> Cappuccino
    CoffeeMachine.manageState(wallet = 2) // Cappuccino -> Finished
    CoffeeMachine.manageState(wallet = 2) // Finished -> Idle

    println("\n--- Cortado que termina con error ---")
    CoffeeMachine.manageState(wallet = 1, selection = Drink.CORTADO)
    CoffeeMachine.manageState(wallet = 1)
    CoffeeMachine.manageState(wallet = 1)
    CoffeeMachine.manageState(wallet = 1, hasErrors = true) // Finished -> Error
    CoffeeMachine.manageState(wallet = 1)                   // sigue en Error

    println("\n--- Limpiando la máquina (sale del error) ---")
    CoffeeMachine.clean()

    println("\n--- Historial ---")
    CoffeeMachine.printHistory()
}
