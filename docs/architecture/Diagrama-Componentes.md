# Diagrama de Componentes 🧩

Este documento detalla la interacción entre los diferentes subsistemas de la aplicación a un nivel de abstracción macro. Se describen los componentes principales y los mecanismos de comunicación que permiten un diseño desacoplado y reactivo.

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/blob/43a529e9c776a896039659e5fda7c6ff1b8dcea4/docs/architecture/assets/Diagrama-Componentes-v1.png" alt="Diagrama de Componentes v1">
</p>

## Núcleo del Sistema: WorldModel 🏰

El componente **WorldModel** representa la autoridad de datos relacionales del sistema, gestionando la persistencia mediante una base de datos PostgreSQL. Se organiza en los siguientes límites de contexto:
- **Usuario**: Gestiona la lógica de perfiles, autenticación y roles de marcas y clientes.
- **Catálogo de Productos**: Administra la estructura del catálogo, incluyendo categorización y etiquetado.
- Este componente garantiza la integridad y consistencia de los datos oficiales de la plataforma.

## Bus de Eventos: Product Observer ✉️

Para evitar el acoplamiento directo entre el núcleo y el motor de búsqueda, se utiliza un bus de eventos que facilita la comunicación asíncrona.
- Al producirse cambios en el catálogo de productos, el componente **Product Publisher** emite notificaciones a través de este bus.
- Este mecanismo permite que el sistema sea extensible, permitiendo que otros módulos se suscriban a estos eventos sin alterar el código del núcleo.

## Subsistema de Búsqueda: SearchEngine 🧠

El componente **SearchEngine** opera de forma independiente, utilizando un motor de búsqueda creado desde cero. Se compone de:
- **Search Facade**: Punto único de entrada que coordina las solicitudes de búsqueda y delegación de tareas.
- **Extracción y Descubrimiento**: Módulo de crawling encargado de la recolección activa de datos.
- **Processing Pipeline**: Línea de procesamiento que incluye el análisis lingüístico (**Linguistic Analyzer**), la indexación (**Indexing Engine**) y la ordenación de resultados (**Ranking Engine**).

## Sincronización y Flujo de Datos 🌊

La sincronización entre el modelo relacional y el motor de búsqueda se realiza mediante un flujo impulsado por eventos:
1. Cualquier modificación en los productos del **WorldModel** activa un evento en el **Product Observer**.
2. El **SearchEngine** actúa como consumidor de estos mensajes, actualizando su índice de forma reactiva.
3. Este flujo asegura que los resultados de búsqueda reflejen los cambios en el modelo de dominio con una latencia mínima.
