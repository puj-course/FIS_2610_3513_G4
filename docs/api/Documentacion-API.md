# 🚀 Guía de Documentación de la API Fashtoll

¡Bienvenido a la sección de documentación de la API! Aquí encontrarás todo lo que necesitas para entender cómo interactuar con los servicios de backend de Fashtoll. 

## 🌟 ¿Qué herramientas usamos?

Para que nuestra API sea fácil de entender y consumir, utilizamos estándares modernos de la industria:

*   **OpenAPI 3.0**: Es el plano arquitectónico de nuestra API. Define cada endpoint, qué datos recibe y qué respuestas devuelve.
*   **Swagger**: Es el motor interno en el backend (Spring Boot) que genera automáticamente este plano basándose en nuestro código.
*   **Scalar**: ¡Nuestra interfaz estrella! Es la página web interactiva donde puedes ver la API de forma profesional, moderna y probar los endpoints.

---

## 📁 Contenido de esta carpeta

En esta sección del repositorio (`docs/api/`) encontrarás tres elementos clave:

1.  **`api-docs.json`** 📜: Es el archivo fuente en formato JSON que contiene toda la especificación OpenAPI. Es el "cerebro" que alimenta a las demás herramientas.
2.  **`API.md`** 📝: Una versión estática en Markdown de la documentación. Es ideal para una consulta rápida directamente desde la interfaz de GitHub sin salir del repositorio.
3.  **`Documentacion-API.md`**: El archivo que estás leyendo ahora mismo.

---

## 🎨 Documentación Interactiva (Scalar)

Para una experiencia **premium** y completa, te recomendamos visitar nuestra página oficial de documentación hospedada en GitHub Pages.

> [!TIP]
> ### 🔗 [Acceder a la Documentación Interactiva de Scalar](https://puj-course.github.io/FIS_2610_3513_G4/)
> *Aquí podrás ver ejemplos de código en múltiples lenguajes, esquemas detallados de los objetos y una navegación mucho más amigable.*

---

## ⚙️ ¿Cómo se genera todo esto?

No tienes que preocuparte por actualizar estos archivos manualmente. Hemos implementado un **Pipeline de Integración Continua (GitHub Actions)** que hace todo el trabajo por nosotros:

1.  Cada vez que se detecta un cambio en el código del `backend`, el pipeline se activa.
2.  Levanta temporalmente la aplicación y los servicios (PostgreSQL y Elasticsearch).
3.  Extrae la especificación más reciente desde Swagger.
4.  Actualiza el archivo `.json`, genera el nuevo `.md` y reconstruye la página web de **Scalar**.

---

## 🛠️ ¿Cómo empezar a probar?

Si eres desarrollador y quieres empezar a integrar esta API:
1.  Entra al enlace de **Scalar** arriba mencionado.
2.  Busca la entidad que te interese (ej. `Product`, `Brand`, `Client`).
3.  Revisa los **Request Bodies** para saber qué datos enviar.
4.  ¡Usa los ejemplos de código generados automáticamente para pegarlos en tu proyecto!

---
*Hecho con ❤️ por el equipo de Fashtoll.*
