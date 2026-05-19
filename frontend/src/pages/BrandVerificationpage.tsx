import { useState } from "react";

type BrandStatus = "Pendiente" | "Verificada" | "Rechazada";

interface Brand {
  id: number;
  name: string;
  description: string;
  owner: string;
  status: BrandStatus;
}

const initialBrands: Brand[] = [
  {
    id: 1,
    name: "Urban Style",
    description: "Marca registrada para venta de ropa urbana y accesorios.",
    owner: "Carlos Ramírez",
    status: "Pendiente",
  },
  {
    id: 2,
    name: "Eco Shoes",
    description: "Marca enfocada en calzado sostenible y productos ecológicos.",
    owner: "Laura Gómez",
    status: "Pendiente",
  },
  {
    id: 3,
    name: "TechWear",
    description: "Marca de productos tecnológicos y accesorios inteligentes.",
    owner: "Andrés Torres",
    status: "Verificada",
  },
  {
    id: 4,
    name: "Fake Store",
    description: "Marca con información incompleta y datos no verificables.",
    owner: "Usuario desconocido",
    status: "Rechazada",
  },
];

function BrandVerificationPage() {
  const [brands, setBrands] = useState<Brand[]>(initialBrands);

  const updateBrandStatus = (id: number, status: BrandStatus) => {
    const updatedBrands = brands.map((brand) =>
      brand.id === id ? { ...brand, status } : brand
    );

    setBrands(updatedBrands);
  };

  const getStatusClass = (status: BrandStatus) => {
    if (status === "Verificada") {
      return "status verified";
    }

    if (status === "Rechazada") {
      return "status rejected";
    }

    return "status pending";
  };

  return (
    <main style={styles.page}>
      <section style={styles.header}>
        <div>
          <h1 style={styles.title}>Panel de Administrador</h1>
          <p style={styles.subtitle}>
            Gestiona la verificación de marcas registradas en la plataforma.
          </p>
        </div>
      </section>

      <section style={styles.tabs}>
        <button style={styles.tabButton}>Usuarios</button>
        <button style={{ ...styles.tabButton, ...styles.activeTab }}>
          Marcas
        </button>
        <button style={styles.tabButton}>Productos</button>
      </section>

      <section style={styles.summaryContainer}>
        <article style={styles.summaryCard}>
          <h3 style={styles.summaryNumber}>
            {brands.filter((brand) => brand.status === "Pendiente").length}
          </h3>
          <p style={styles.summaryText}>Pendientes</p>
        </article>

        <article style={styles.summaryCard}>
          <h3 style={styles.summaryNumber}>
            {brands.filter((brand) => brand.status === "Verificada").length}
          </h3>
          <p style={styles.summaryText}>Verificadas</p>
        </article>

        <article style={styles.summaryCard}>
          <h3 style={styles.summaryNumber}>
            {brands.filter((brand) => brand.status === "Rechazada").length}
          </h3>
          <p style={styles.summaryText}>Rechazadas</p>
        </article>
      </section>

      <section style={styles.content}>
        <div style={styles.contentHeader}>
          <h2 style={styles.sectionTitle}>Verificación de marcas</h2>
          <p style={styles.sectionDescription}>
            Revisa la información de cada marca y decide si debe ser aprobada o
            rechazada.
          </p>
        </div>

        <div style={styles.brandList}>
          {brands.map((brand) => (
            <article key={brand.id} style={styles.brandCard}>
              <div style={styles.brandInfo}>
                <div style={styles.brandTop}>
                  <h3 style={styles.brandName}>{brand.name}</h3>
                  <span className={getStatusClass(brand.status)}>
                    {brand.status}
                  </span>
                </div>

                <p style={styles.brandDescription}>{brand.description}</p>

                <p style={styles.owner}>
                  <strong>Responsable:</strong> {brand.owner}
                </p>
              </div>

              <div style={styles.actions}>
                <button
                  style={{ ...styles.actionButton, ...styles.approveButton }}
                  onClick={() => updateBrandStatus(brand.id, "Verificada")}
                >
                  Aprobar marca
                </button>

                <button
                  style={{ ...styles.actionButton, ...styles.rejectButton }}
                  onClick={() => updateBrandStatus(brand.id, "Rechazada")}
                >
                  Rechazar marca
                </button>
              </div>
            </article>
          ))}
        </div>
      </section>

      <style>
        {`
          .status {
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 13px;
            font-weight: 700;
          }

          .verified {
            background-color: #dcfce7;
            color: #166534;
          }

          .rejected {
            background-color: #fee2e2;
            color: #991b1b;
          }

          .pending {
            background-color: #fef3c7;
            color: #92400e;
          }
        `}
      </style>
    </main>
  );
}

const styles: Record<string, React.CSSProperties> = {
  page: {
    minHeight: "100vh",
    backgroundColor: "#f4f6f8",
    padding: "32px",
    fontFamily: "Arial, sans-serif",
  },
  header: {
    backgroundColor: "#111827",
    color: "#ffffff",
    padding: "32px",
    borderRadius: "18px",
    marginBottom: "24px",
  },
  title: {
    margin: 0,
    fontSize: "34px",
    fontWeight: 800,
  },
  subtitle: {
    marginTop: "10px",
    fontSize: "16px",
    color: "#d1d5db",
  },
  tabs: {
    display: "flex",
    gap: "12px",
    marginBottom: "24px",
  },
  tabButton: {
    border: "none",
    padding: "12px 22px",
    borderRadius: "12px",
    backgroundColor: "#ffffff",
    cursor: "pointer",
    fontWeight: 700,
    color: "#374151",
    boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
  },
  activeTab: {
    backgroundColor: "#2563eb",
    color: "#ffffff",
  },
  summaryContainer: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
    gap: "18px",
    marginBottom: "28px",
  },
  summaryCard: {
    backgroundColor: "#ffffff",
    padding: "22px",
    borderRadius: "16px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.08)",
  },
  summaryNumber: {
    margin: 0,
    fontSize: "30px",
    color: "#111827",
  },
  summaryText: {
    margin: 0,
    marginTop: "6px",
    color: "#6b7280",
    fontWeight: 600,
  },
  content: {
    backgroundColor: "#ffffff",
    padding: "28px",
    borderRadius: "18px",
    boxShadow: "0 4px 16px rgba(0,0,0,0.08)",
  },
  contentHeader: {
    marginBottom: "22px",
  },
  sectionTitle: {
    margin: 0,
    fontSize: "26px",
    color: "#111827",
  },
  sectionDescription: {
    marginTop: "8px",
    color: "#6b7280",
  },
  brandList: {
    display: "flex",
    flexDirection: "column",
    gap: "18px",
  },
  brandCard: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: "20px",
    padding: "22px",
    borderRadius: "16px",
    border: "1px solid #e5e7eb",
    backgroundColor: "#f9fafb",
  },
  brandInfo: {
    flex: 1,
  },
  brandTop: {
    display: "flex",
    alignItems: "center",
    gap: "12px",
    marginBottom: "8px",
  },
  brandName: {
    margin: 0,
    fontSize: "21px",
    color: "#111827",
  },
  brandDescription: {
    margin: 0,
    marginBottom: "10px",
    color: "#4b5563",
  },
  owner: {
    margin: 0,
    color: "#374151",
    fontSize: "14px",
  },
  actions: {
    display: "flex",
    gap: "10px",
    flexWrap: "wrap",
  },
  actionButton: {
    border: "none",
    padding: "11px 16px",
    borderRadius: "10px",
    cursor: "pointer",
    fontWeight: 700,
  },
  approveButton: {
    backgroundColor: "#16a34a",
    color: "#ffffff",
  },
  rejectButton: {
    backgroundColor: "#dc2626",
    color: "#ffffff",
  },
};

export default BrandVerificationPage;