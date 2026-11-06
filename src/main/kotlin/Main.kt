package org.example

/** Prueba de la cafetera: camino normal, saldo insuficiente y error. */
fun main() {
    println("--- Cappuccino con saldo insuficiente ---")
    CoffeeMachine.gestorEstados(monedero = 1, seleccion = Bebida.CAPPUCCINO)
    CoffeeMachine.gestorEstados(monedero = 1)

    println("\n--- Con saldo suficiente ---")
    CoffeeMachine.gestorEstados(monedero = 2)
    CoffeeMachine.gestorEstados(monedero = 2)
    CoffeeMachine.gestorEstados(monedero = 2)

    println("\n--- Cortado que termina con error ---")
    CoffeeMachine.gestorEstados(monedero = 1, seleccion = Bebida.CORTADO)
    CoffeeMachine.gestorEstados(monedero = 1)
    CoffeeMachine.gestorEstados(monedero = 1)
    CoffeeMachine.gestorEstados(monedero = 1, hayErrores = true)

    println("\n--- Limpiando la máquina ---")
    CoffeeMachine.clean()
}
