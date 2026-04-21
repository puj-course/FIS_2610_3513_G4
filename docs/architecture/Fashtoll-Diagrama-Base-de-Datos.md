# Diagrama de Base de Datos de Fashtoll

Esta imagen muestra una versión inicial del diagrama de base de datos diseñado para el sistema a partir de las historias de usuario definidas hasta el momento. Se resaltan tres entidades principales: Clientes (clients), Marcas (brands) y Productos (products). Las entidades Cliente y Marca son una "especialización" de la entidad Usuario (users) que reúne las credenciales de sus cuentas para el acceso al sistema (registro e inicio de sesión). Otras entidades con un rol secundario son: Tipo de Producto (product_type), Imágenes de Producto (product_images), Lista de deseados (wishlists), Etiquetas (tags), Reseñas de Productos (product_reviews) y Reseñas de Marcas (brand_reviews). Por último, están las entidades que representan relaciones "muchos a muchos" entre otras dos entidades, como: Seguidores de Marcas (client_follows_brand), Productos en Lista de Deseados (wishlist_saves_product), Etiquetas de Marcas (brand_tags) y Etiquetas de Productos (product_tags).

Este es el modelo que se está implementando inicialmente en el backend del proyecto con Spring Boot, PostgreSQL y Flyway.

<p align="center">
  <img src="https://github.com/puj-course/FIS_2610_3513_G4/blob/43cef85e042c323f39dba6ffd311bbbc45b4e979/docs/architecture/assets/DB-Diagram-v1.png" alt="Fashtoll DB Diagram v1" width="1000">
</p>

