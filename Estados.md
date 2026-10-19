# Máquina de estados

| Diagrama | Estado en el código |
|---|---|
| E0 - ERROR | `Error(message, origen)` |
| E1 - Esperando | `Idle(timestamp)` |
| E2 - Procesando selección | `ProcessingSelection(seleccion)` |
| Elaborando (*) | `MakingCoffee` → `Cortado`, `Largo`, `Cappuccino` |
| E3 - Terminado | `Finished(bebida)` |

## Diagrama de estados

```mermaid
stateDiagram-v2
    [*] --> Idle

    Idle --> Idle : gestorEstados(seleccion == null)
    Idle --> ProcessingSelection : gestorEstados(seleccion != null)

    ProcessingSelection --> ProcessingSelection : monedero < precio (muestra precio)

    state MakingCoffee {
        Cortado
        Largo
        Cappuccino
    }

    ProcessingSelection --> Cortado : monedero >= precio
    ProcessingSelection --> Largo : monedero >= precio
    ProcessingSelection --> Cappuccino : monedero >= precio

    Cortado --> Finished : gestorEstados()
    Largo --> Finished : gestorEstados()
    Cappuccino --> Finished : gestorEstados()

    Finished --> Idle : gestorEstados(sin errores)
    Finished --> Error : gestorEstados(hayErrores)
    Error --> Error : gestorEstados()

    Idle --> Idle : clean()
    ProcessingSelection --> Idle : clean()
    MakingCoffee --> Idle : clean()
    Finished --> Idle : clean()
    Error --> Idle : clean()
```
