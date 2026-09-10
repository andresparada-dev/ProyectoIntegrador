# Bitácora - Semana [1]

## 1. Datos de la actividad

- **Estudiantes:** Andrés Parada, Maddox Valbuena, Julián Cárdenas
- **Equipo:** Si Team
- **Semana:** 1
- **Fecha del laboratorio:** 2026-09-07
- **Fecha del taller:** 2026-09-07
- **Tema principal:** Ingesta confiable
- **Pregunta de la semana:**  Si un sensor reporta basura, ¿tu programa se cae, miente, o avisa?

## 2. Prediccion antes de ejecutar

Antes de abrir o ejecutar el programa, responde:

1. **Que creo que va a ocurrir?**
   Creo que si no encapsulamos correctamente la clase que representa la lectura, ingresar un dato anómalo creará un objeto con un estado inválido . Si ponemos una validación pero no manejamos la excepción, el programa se detendrá abruptamente (se cae). Espero lograr que el método *setter* del objeto rechace el valor y lance un error controlado que el programa principal atrape para registrarlo (avisa) sin detenerse.

2. **Que parte del programa o del algoritmo puede fallar?**
   El método `setValor()` dentro de la clase del sensor y el bloque del ciclo principal (`main`) que instancia los objetos, donde se debe gestionar el error.

3. **Como comprobare mi prediccion?**
   Instanciando un objeto de la clase y pasándole intencionalmente un valor fuera de rango (ej. 1500) a través de su constructor o *setter*, para ver si el estado interno se modifica y si el programa aborta la ejecución o continúa.

## 3. Evidencia del laboratorio

### Resultado observado

Al pasarle el dato basura (valor: 1500), la clase respetó su encapsulamiento. El método `setValor()` evaluó la condición y se negó a asignar el valor al atributo privado de la clase. El programa principal registró un mensaje en consola: "Alerta: Intento de asignación inválida descartado", y el objeto mantuvo su estado previo seguro. 

### Diferencia entre la prediccion y el resultado

El comportamiento de la clase fue el esperado en cuanto a proteger su estado. Sin embargo, al principio el programa sí se cayó. Habíamos programado el *setter* para lanzar un `IllegalArgumentException`, pero en nuestra predicción omitimos que debíamos tener una estructura para atrapar esa excepción en la clase principal.

### Error o comportamiento inesperado

- **Que ocurrio?** Al inyectar el dato erróneo, el programa crasheó lanzando una excepción no controlada en la consola, deteniendo por completo el ciclo de lectura de los demás sensores.
- **Por que ocurrio?** El objeto hizo su trabajo al rechazar el dato lanzando una excepción para proteger su encapsulamiento, pero la clase principal (el controlador) no tenía un bloque `try-catch` para manejar esa excepción.
- **Como lo corregimos o que falta corregir?** Envolvimos la instanciación y asignación de valores en el ciclo principal dentro de un bloque `try-catch`. Ahora, cuando el objeto lanza la excepción, el `catch` la intercepta, imprime el log del error (avisa) y permite que el ciclo continúe iterando.

## 4. Explicacion en lenguaje llano

Explica el concepto principal como se lo explicarias a una persona de doce
anos. Usa entre tres y cinco lineas y evita palabras tecnicas que no expliques.

> Imagina una alcancía inteligente que solo acepta monedas. Si intentas meterle un botón de plástico (el dato basura), la alcancía no se rompe (el programa no se cae) y tampoco lo cuenta como dinero real (el programa no miente). Simplemente escupe el botón y hace un sonido de alerta (el programa avisa), quedando lista para recibir la siguiente moneda.

### Ejemplo o analogia

La alcancía inteligente representa nuestro objeto y el mecanismo que escupe el botón es su encapsulamiento. Dejar que el botón entre y se cuente como dinero sería tener variables públicas sin protección. Romper la alcancía a martillazos sería un "crash" del sistema por no saber manejar el error. La analogía deja de ser exacta porque en programación un objeto que falla en su creación a veces es destruido por el sistema, mientras que la alcancía siempre sigue ahí físicamente.

## 5. El vacio que encontre

Al intentar explicar el tema, identifica el punto que aun no comprendes bien.

- **Mi duda concreta es:** ¿Cómo evitamos repetir el mismo bloque `try-catch` en la clase principal si en el futuro tenemos 20 tipos de sensores distintos con reglas de validación completamente diferentes?
- **Lo que ya puedo explicar es:** Cómo proteger los atributos de una clase haciéndolos privados y usando *getters* y *setters* con condicionales lógicos para evitar estados corruptos.
- **Para resolver la duda consulte:** El material de clase sobre herencia y polimorfismo, y discutí con mis compañeros.
- **Ahora lo entiendo asi:** Entiendo que podemos crear una clase padre abstracta (o una interfaz) llamada `Sensor` con un método polimórfico `validar()`. Así, el ciclo principal puede tratar a todos los sensores por igual y atrapar un solo tipo de excepción genérica, mientras cada clase hija define sus propias reglas internamente.

## 6. Trazado de la solucion

Escoge una ejecucion, recorrido o caso representativo y trazalo paso a paso.
Incluye los valores importantes despues de cada paso.

| Paso | Estado de los datos o estructura | Decision o resultado |
|---|---|---|
| 1 | Bucle en el `main` recibe `1450` | Llama al método `sensor.setValor(1450)` |
| 2 | Ejecución dentro del objeto `sensor` | Evalúa regla de negocio: `if (valor < 0 || valor > 1023)` |
| 3 | Condición se cumple (`true`) | `setValor` lanza `new IllegalArgumentException("Fuera de rango")` |
| 4 | El control regresa al `main` | El bloque `catch` atrapa el error, imprime alerta y continúa el ciclo |

## 7. Decision de diseño

Relaciona lo aprendido con la Plataforma de Monitoreo Ambiental Urbano.

- **Problema que debiamos resolver:** Evitar que datos corruptos de sensores dañen la consistencia de la información en el sistema, asegurando que el programa no colapse en tiempo de ejecución.
- **Estructura, algoritmo o estrategia elegida:** Encapsulamiento estricto (atributos `private`) combinado con manejo de excepciones (`try-catch`).
- **Alternativa descartada:** Dejar los atributos como `public` y hacer las validaciones de rango (el `if`) directamente en el `main` antes de guardar el dato.
- **Por que elegimos la primera:** Por el Principio de Responsabilidad Única. El `main` no debe saber cuáles son los rangos válidos de un sensor específico; es el propio objeto quien debe ser responsable de proteger la integridad de su estado interno.
- **Que evidencia respalda la decision:** Al intentar inicializar objetos defectuosos desde otras partes del código en nuestras pruebas, la clase rechazó automáticamente los datos basuras sin importar de dónde vinieran. Si hubiéramos validado en el `main`, cualquier otro módulo podría haber ingresado basura al objeto.

## 8. Aporte al proyecto

- **Archivo(s) o modulo(s) trabajado(s):** Clase del modelo (`SensorAmbiente`) y el controlador principal (`Main`).
- **Cambio realizado:** Cambio de visibilidad de atributos a `private`, reescritura de los *setters* para incluir validaciones y lanzamiento de excepciones, e implementación del `try-catch` en el flujo de control.
- **Como se conecta con la capa anterior:** Los objetos ahora actúan como guardianes autónomos; cuando la capa de conexión intenta inyectarles datos, ellos deciden si el valor es válido para transformarse en información útil o si lo rechazan.
- **Que queda pendiente para la siguiente semana:** Implementar herencia para separar la lógica entre un `SensorTemperatura` y un `SensorHumedad`, ya que tendrán rangos de validación diferentes.

## 9. Commits realizados

Registra los commits que muestran tu aporte individual.

| Commit | Mensaje | Que demuestra |
|---|---|---|
| `e4d7a9f` | `feat: encapsular atributos de Sensor y agregar validacion en setValor` | Protección del estado interno del objeto (evita que el programa mienta). |
| `b2c8f1e` | `fix: envolver instanciacion en try-catch en el main` | Manejo correcto de la excepción (evita que el programa se caiga y permite que avise). |

## 10. Reexplicacion final

Despues del taller, vuelve a responder la pregunta de la semana en cinco lineas
o menos. Esta respuesta debe ser mas precisa que la de la seccion 4 y debe
incluir la razon de tu decision tecnica.

> Ante la basura, el programa no debe caerse ni mentir; debe **avisar**. Técnicamente, logramos esto mediante el encapsulamiento: el objeto protege sus atributos privados y, mediante su *setter*, rechaza el dato inválido lanzando una excepción (no miente). El controlador principal envuelve esta interacción en un `try-catch`, lo que le permite atrapar el error, registrarlo (avisa) y continuar su ejecución ininterrumpida (no se cae).

## 11. Reflexion individual

Responde con honestidad:

1. **Lo que ahora puedo hacer y antes no podia:**
   Proteger el estado interno de mis objetos (encapsulamiento) para asegurar que ninguna otra parte del código pueda inyectarles valores absurdos y corromper su lógica.
2. **El error o supuesto que mas me enseno:**
   Creer que lanzar un error (`Exception`) dentro de una clase era suficiente. Aprendí que si lanzo un error y no hay un mecanismo que lo atrape (un `catch`), el error se propaga hasta reventar la ejecución completa del programa.
3. **La pregunta que llevaria a la proxima clase:**
   ¿Cuál es la convención en Java para crear nuestras propias clases de excepciones personalizadas (ej. `SensorInvalidoException`) en lugar de depender de excepciones genéricas como `IllegalArgumentException`?
4. **Que parte del trabajo fue realmente mia:**
   El rediseño estructural de la clase: convertir variables públicas a privadas, diseñar la lógica de los *setters* y explicarle al equipo por qué el controlador (`main`) era el responsable de atrapar los errores y no de validarlos.
5. **¿Se uso IA?**: 
   Sí, se utilizó IA en la verificación del programa y consejos adicionales cuanto al proyecto y la bitácora. También al haber empezado la bitácora luego del desarrollo y no "durante" el desarrollo; tuvimos que rellenar secciones que no recordábamos como responder porque los cambios ya habían ocurrido, para así entregar la bitácora en su totalidad (será corregido esto en las bitácoras de las próximas semanas).

## Lista de verificacion antes de entregar

- [x] Escribi la prediccion antes de consultar el resultado.
- [x] Inclui evidencia concreta del laboratorio.
- [x] Explique un concepto sin depender de jerga.
- [x] Registre un vacio, una duda o un error real.
- [x] Trace al menos un caso paso a paso.
- [x] Justifique una decision del proyecto y una alternativa descartada.
- [x] Registre mis commits y mi aporte individual.
- [x] Deje claro que queda pendiente.
- [x] Renombre el archivo con el formato `sXX-nombre.md`.
