# Arquitectura del Sistema Fashtoll - Diagrama de Clases

Este documento detalla la estructura lógica, la arquitectura orientada a eventos y la evolución del diagrama de clases del sistema **Fashtoll**. El diseño final refleja una arquitectura robusta, pensada para desacoplar el núcleo del negocio de la infraestructura de búsqueda.

---

## 1. Visión General de la Arquitectura (General)

El diagrama de clases de Fashtoll no es un simple reflejo del código fuente, sino un "mapa del tesoro" que aísla la **Lógica de Negocio** de la "fricción" técnica (como DTOs, Controllers y Mappers). La arquitectura se basa en el patrón arquitectónico de **Capas (N-Tier)** con una fuerte influencia de **Domain-Driven Design (DDD)**.

Los pilares fundamentales del diseño son:
* **Separación de Responsabilidades:** Cada clase tiene un único propósito. Las entidades manejan el estado, los servicios orquestan el comportamiento y los repositorios abstraen la persistencia.
* **Controladores Delgados (Thin Controllers):** La lógica de toma de decisiones se ha desplazado completamente hacia la capa de Servicios, dejando a los controladores web únicamente con la responsabilidad de enrutamiento HTTP.
* **Desacoplamiento mediante Eventos (EDA):** El uso del patrón *Observer* permite que el catálogo de productos evolucione sin conocer explícitamente los mecanismos internos del motor de búsqueda.

---

## 2. Componentes Específicos del Sistema (Específico)

### Capa de Dominio (`worldModel`)
Es el corazón matemático del sistema. Aquí residen los sustantivos del dominio:
* **Herencia de Usuarios:** `Brand` y `Client` extienden de `User`, consolidando la identidad y las credenciales en una base común, pero especializando el comportamiento y perfilamiento.
* **Tipado Finito:** El uso de Enumeradores (`Color`, `Gender`, `GeneralFit`, `Category`) actúa como un conjunto cerrado de restricciones, asegurando la integridad de los datos de los productos antes de que lleguen a la base de datos o al motor de búsqueda.

### Capa de Servicios (`services`)
Actúa como el orquestador principal:
* **`BrandService` & `ProductService`:** Coordinan flujos complejos. Por ejemplo, `BrandService` no solo crea marcas, sino que colabora con `ProductService` para gestionar el inventario propio (`MyProducts`), estableciendo una clara jerarquía de delegación.
* **Interacciones Transversales:** Se apoyan en el `UserService` para operaciones compartidas como el cambio de contraseñas.

### El Motor de Búsqueda y el Patrón Observer
La integración con Elasticsearch se maneja de forma elegante para evitar un acoplamiento fuerte:
* **Originador (`ProductEventPublisher`):** Cuando `ProductService` modifica un producto, emite un evento al aire.
* **Suscriptor (`ElasticSearchProductObserver`):** Escucha silenciosamente estos eventos y se encarga de formatear y enviar el `ProductDocument` a Elasticsearch a través del `ProductSearchService`.
* Este diseño asegura que si el equipo decide cambiar la tecnología de búsqueda en el futuro, el `ProductService` no sufrirá ninguna modificación.

---

## 3. Evolución del Diagrama (Versionado)

A lo largo del ciclo de desarrollo, la arquitectura se fue refinando, pasando de acoplamientos fuertes y atributos genéricos (Strings) a un modelo fuertemente tipado y orquestado por servicios.

### Version 1:

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/raw/main/docs/architecture/assets/v1%20-%20Fashtoll%20Class%20Diagram.png" alt="Fashtoll class diagram v1" width="1000">
</p>

### Version 2:

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/raw/main/docs/architecture/assets/v2%20-%20Fashtoll%20Class%20Diagram.png" alt="Fashtoll class diagram v2" width="1000">
</p>

### Version 3:

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/raw/main/docs/architecture/assets/v3%20-%20Fashtoll%20Class%20Diagram.png" alt="Fashtoll class diagram v3" width="1000">
</p>

### Version 4:

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/raw/main/docs/architecture/assets/v4%20-%20Fashtoll%20Class%20Diagram.png" alt="Fashtoll class diagram v4" width="1000">
</p>

### Version 5:

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/raw/main/docs/architecture/assets/v5%20-%20Fashtoll%20Class%20Diagram.png" alt="Fashtoll class diagram v5" width="1000">
</p>
