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
- **Frontend:** TypeScript, React, Vite, Tailwind CSS, Axios
- **Backend:** Java, Spring Boot, Spring Security
- **Construcción:** Maven
- **Base de Datos:** PostgreSQL, Flyway, Elasticsearch
- **DevOps:** GitHub Actions, Docker, SonarQube
- **Control de versiones:** Git
- **Otros:** JWT, OpenAPI, Swagger

---

## 🗂️ Estructura del Proyecto
```text
FIS_2610_3513_G4/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows/
│       ├── ci.yml
│       └── cd.yml
├── conf/
│   ├── config.yaml
│   └── settings.json
├── docs/
│   ├── api/
│   ├── architecture/
│   ├── design/
│   ├── dynamic view/
│   ├── learning process/
│   ├── requirements/
│   ├── Scrum/
│   ├── user_guide/
│   └── Presentación-Inicial-Proyecto.pdf/
├── jupyter/
│   ├── notebooks/
│   │   ├── exploration.ipynb
│   │   └── analysis.ipynb
│   └── datasets/
│       ├── data1.csv
│       └── data2.csv
├── scripts/
│   ├── setup.sh
│   ├── deploy.sh
│   └── test.sh
├── backend/                 # Beckend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   |   ├── java/
│   │   |   └── resources/
│   │   └── test/
│   │       └── java/
│   └── pom.xml
├── frontend/                # Frontend (React)
│   ├── public/
│   └── src/
├── temp/
│   ├── temp_file.txt
│   └── temp_data/
│       ├── temp1.tmp
│       └── temp2.tmp
├── .gitignore
├── README.md
├── LICENSE
├── CHANGELOG.md
├── CONTRIBUTING.md
├── Dockerfile
├── docker-compose.yml
├── flyway.conf
└── Makefile
```

---

## 📥 Instalación y Ejecución
**Requisitos**
- Java 25
- Maven
- Docker Desktop
- Node.js 18+

## 💻 Clonar el repositorio
```text
git clone https://github.com/puj-course/FIS_2610_3513_G4.git
cd FIS_2610_3513_G4
```

## 🐋 Ejecución de PostgreSQL y Elasticsearch con Docker
Para desplegar los servicios de base de datos PostgreSQL e indexación de Elasticsearch, se debe instalar Docker Desktop y ejecutar este comando en la raíz del proyecto:
```text
docker compose up -d
```
La base de datos PostgreSQL estará disponible en el puerto 5432. El índice de Elasticsearch estará disponible en el puerto 9200.

## ▶️ Ejecución de la API Spring Boot con IDE
Abrir el proyecto con cualquier IDE compatible y ejecutarlo. En IntelliJ IDEA:
```text
Open Project -> FIS_2610_3513_G4 -> Run 'FashtollApplication'
```
La API estará disponible en el puerto 8080.

### 📚 Documentación de la API en Scalar
Adicionalmente, puedes abrir [esta página](https://puj-course.github.io/FIS_2610_3513_G4/) que muestra la documentación de la API a partir de OpenAPI y Swagger. Es una interfaz llamativa y moderna en Scalar para conocer cómo funciona la API, sus endpoints y empezar a probarla rápidamente. Este sitio se genera automáticamente gracias GitHub Pages y un pipeline de itegración continua con GitHub Actions.

## 🖼️ Ejecución del frontend con npm
Ejecutar los siguientes comandos desde la raíz del proyecto para correr la aplicación React.
```text
cd frontend
npm install
npm run build
npm run dev
```
La interfaz de inicio estará disponible en la ruta http://localhost:5173/.

## 🧪 Ejecución de pruebas (Próximamente)
```text
// próximamente disponible...
docker-compose run backend mvn test
```

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
