# Fashtoll by CEIBA

<p align="center">
  <img src="./docs/design/branding/fashtoll-logo-v1.png" alt="FashToll Logo v1" width="1000">
</p>

**Fashtoll** es el ecosistema digital diseñado para conectar el talento de la moda local con el consumidor consciente. Actuamos como un puente inteligente que elimina la fatiga de decisión y potencia la visibilidad de marcas emergentes mediante tecnología de búsqueda avanzada.

---
## 👥 Equipo del Proyecto

| Nombre | Rol Scrum | Rol Técnico | GitHub / Perfil |
| :--- | :--- | :--- | :--- |
| David Romero | **Scrum Master y QA Lead** | Gestión / Liderazgo DB | https://github.com/davidr124 |
| Alejandro González | **Scrum Master y Sprint Planner** | Arquitecto de SW y Backend Developer | https://github.com/alejandrogonzalezo1055 |
| Nicolas Joya | **Product Owner y Configuration Manager** | Backend Developer | https://github.com/NicoJoya |
| Juan Sebastian Ruiz | **Sprint Planner y DevOps Engineer** | Frontend Developer | https://github.com/Juanseruiz07 |

---

## 🛠️ Tecnologías Utilizadas
- **Frontend:** TypeScript, React, Vite, Tailwind CSS, Framer Motion, Lucide React, React Router, React Query, Axios
- **Backend:** Java, Spring Boot, Spring Security, Twilio
- **Base de Datos:** PostgreSQL, Flyway
- **Construcción y Calidad:** Maven, JUnit, Mockito, Jacoco, PMD, ESLint, PostCSS
- **DevOps:** GitHub Actions, Docker, SonarQube
- **Control de versiones:** Git
- **Documentación y Otros:** JWT, OpenAPI, Swagger, Scalar

---

## 🗂️ Estructura del Proyecto
La siguiente estructura resalta las carpetas y archivos más importantes para el desarrollo y despliegue del proyecto:

```text
FIS_2610_3513_G4/
├── .github/                 # Workflows de CI/CD (GitHub Actions) e Issue/PR templates
├── backend/                 # API RESTful (Java, Spring Boot, Maven)
│   ├── src/main/java/       # Código fuente del backend
│   ├── src/test/java/       # Código fuente de las pruebas del backend
│   ├── Dockerfile           # Dockerfile para el backend
│   └── pom.xml              # Configuración de dependencias Maven
├── frontend/                # Aplicación Web (React, Vite, TypeScript)
│   ├── src/                 # Componentes, vistas y lógica del frontend
│   ├── Dockerfile           # Dockerfile para el frontend
│   └── package.json         # Configuración y dependencias de npm
├── conf/                    # Archivos de configuración general
├── docs/                    # Documentación del proyecto (API, arquitectura, diseño, Scrum)
├── jupyter/                 # Notebooks de Jupyter y datasets para análisis
├── scripts/                 # Scripts de utilidad (setup, deploy, test)
├── docker-compose.yml       # Orquestación de servicios (PostgreSQL, backend, frontend)
└── README.md                # Documentación principal
```

---

## 📥 Instrucciones de Uso

### 1. Requisitos Previos
- **Java 21**
- **Maven**
- **Docker Desktop** (para base de datos)
- **Node.js 20+** y **npm**
- **Git**

### 2. Clonar el Repositorio
```bash
git clone https://github.com/puj-course/FIS_2610_3513_G4.git
cd FIS_2610_3513_G4
```

### 3. Levantar Servicios (Base de Datos)
Para desplegar PostgreSQL, asegúrate de tener Docker Desktop abierto y ejecuta en la raíz del proyecto:
```bash
docker compose up -d
```
- **PostgreSQL** estará disponible en el puerto `5432`.

### 4. Ejecución del Backend (API Spring Boot)
**Opción A: Usando un IDE (IntelliJ IDEA, Eclipse, VS Code)**
1. Abre el proyecto en tu IDE y selecciona la carpeta `backend` o el proyecto raíz.
2. Espera a que Maven descargue las dependencias.
3. Ejecuta la clase principal `FashtollApplication`.

**Opción B: Usando Maven desde la terminal**
```bash
cd backend
mvn spring-boot:run
```
La API estará disponible en el puerto `8080`.

### 5. Ejecución del Frontend (React + Vite)
En una nueva terminal, navega a la carpeta del frontend para instalar las dependencias y ejecutar la aplicación:
```bash
cd frontend
npm install
npm run dev
```
La interfaz web estará disponible en `http://localhost:5173/`.

### 6. Ejecución de Pruebas
Para ejecutar las pruebas unitarias y de integración del backend:
```bash
cd backend
mvn test
```

### 7. Uso de la Aplicación y Documentación
Una vez que el backend y frontend estén en ejecución, puedes interactuar con Fashtoll de varias formas:

- **Frontend (UI):** Abre `http://localhost:5173/` en tu navegador para usar la interfaz gráfica.
- **Documentación de la API (Scalar):** Puedes explorar e interactuar con los endpoints visitando [nuestra documentación oficial](https://puj-course.github.io/FIS_2610_3513_G4/). Esta interfaz moderna (creada con Scalar a partir del workflow docs.yml) permite conocer la API y probarla rápidamente.
- **Swagger UI (Local):** Si prefieres acceder localmente, ingresa a `http://localhost:8080/swagger-ui.html` mientras el backend está en ejecución.
- **Postman:** Puedes importar la colección apuntando a `http://localhost:8080/v3/api-docs` o crear peticiones directamente hacia los endpoints.

---

## 🎓 Contexto Académico
- **Asignatura:** Fundamentos de Ingeniería de Software
- **Docente:** Luis Gabriel Moreno Sandoval, PhD
- **Contacto:** morenoluis@javeriana.edu.co

---

## 📧 Contacto

**Equipo de desarrollo:**

**Nicolás Joya Murillo**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 nicolas_joya@javeriana.edu.co 

**Alejandro Gonzalez Ochoa**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 alejandrogonzalezo@javeriana.edu.co

**Juan Sebastián Ruiz**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 Juan.Ruizg@javeriana.edu.co

**David Felipe Mannios Romero**  
Estudiante de Ingeniería en Sistemas, Pontificia Universidad Javeriana  
📧 david.manniosr@javeriana.edu.co

---

## ⚖️ Licencia
Proyecto desarrollado con fines académicos.
