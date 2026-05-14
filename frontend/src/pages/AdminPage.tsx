import React, { useState } from "react";

type User = {
  id: number;
  name: string;
  email: string;
  role: "admin" | "cliente";
};

type Brand = {
  id: number;
  name: string;
  status: "pendiente" | "verificada";
};

type Product = {
  id: number;
  name: string;
  brand: string;
  status: "activo" | "invalido";
};

const AdminPage: React.FC = () => {
  const [users] = useState<User[]>([
    {
      id: 1,
      name: "Juan Sebastián",
      email: "juan@example.com",
      role: "admin",
    },
    {
      id: 2,
      name: "Nicolás Esteban",
      email: "nicolas@example.com",
      role: "cliente",
    },
  ]);

  const [brands, setBrands] = useState<Brand[]>([
    {
      id: 1,
      name: "Nike",
      status: "pendiente",
    },
    {
      id: 2,
      name: "Adidas",
      status: "verificada",
    },
  ]);

  const [products, setProducts] = useState<Product[]>([
    {
      id: 1,
      name: "Camisa deportiva",
      brand: "Nike",
      status: "activo",
    },
    {
      id: 2,
      name: "Producto sin información",
      brand: "Marca desconocida",
      status: "invalido",
    },
  ]);

  const verifyBrand = (brandId: number) => {
    const updatedBrands = brands.map((brand) =>
      brand.id === brandId ? { ...brand, status: "verificada" as const } : brand
    );

    setBrands(updatedBrands);
  };

  const deleteProduct = (productId: number) => {
    const updatedProducts = products.filter(
      (product) => product.id !== productId
    );

    setProducts(updatedProducts);
  };

  return (
    <main style={styles.container}>
      <section style={styles.header}>
        <h1 style={styles.title}>Panel de Administrador</h1>
        <p style={styles.subtitle}>
          Desde esta página el administrador puede gestionar usuarios, marcas y
          productos de la plataforma.
        </p>
      </section>

      <section style={styles.grid}>
        <article style={styles.card}>
          <h2 style={styles.cardTitle}>Usuarios registrados</h2>

          <div style={styles.tableContainer}>
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>Nombre</th>
                  <th style={styles.th}>Correo</th>
                  <th style={styles.th}>Rol</th>
                </tr>
              </thead>

              <tbody>
                {users.map((user) => (
                  <tr key={user.id}>
                    <td style={styles.td}>{user.name}</td>
                    <td style={styles.td}>{user.email}</td>
                    <td style={styles.td}>
                      <span
                        style={{
                          ...styles.badge,
                          backgroundColor:
                            user.role === "admin" ? "#dbeafe" : "#f3f4f6",
                          color: user.role === "admin" ? "#1d4ed8" : "#374151",
                        }}
                      >
                        {user.role}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </article>

        <article style={styles.card}>
          <h2 style={styles.cardTitle}>Gestión de marcas</h2>

          <div style={styles.list}>
            {brands.map((brand) => (
              <div key={brand.id} style={styles.listItem}>
                <div>
                  <h3 style={styles.itemTitle}>{brand.name}</h3>
                  <p style={styles.itemText}>
                    Estado:{" "}
                    <strong>
                      {brand.status === "verificada"
                        ? "Verificada"
                        : "Pendiente"}
                    </strong>
                  </p>
                </div>

                {brand.status === "pendiente" && (
                  <button
                    style={styles.primaryButton}
                    onClick={() => verifyBrand(brand.id)}
                  >
                    Verificar marca
                  </button>
                )}
              </div>
            ))}
          </div>
        </article>

        <article style={styles.card}>
          <h2 style={styles.cardTitle}>Gestión de productos</h2>

          <div style={styles.list}>
            {products.map((product) => (
              <div key={product.id} style={styles.listItem}>
                <div>
                  <h3 style={styles.itemTitle}>{product.name}</h3>
                  <p style={styles.itemText}>Marca: {product.brand}</p>
                  <p style={styles.itemText}>
                    Estado:{" "}
                    <strong>
                      {product.status === "activo" ? "Activo" : "Inválido"}
                    </strong>
                  </p>
                </div>

                {product.status === "invalido" && (
                  <button
                    style={styles.dangerButton}
                    onClick={() => deleteProduct(product.id)}
                  >
                    Eliminar producto
                  </button>
                )}
              </div>
            ))}
          </div>
        </article>
      </section>
    </main>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    minHeight: "100vh",
    backgroundColor: "#f4f6f8",
    padding: "40px",
    fontFamily: "Arial, sans-serif",
  },
  header: {
    marginBottom: "32px",
  },
  title: {
    fontSize: "32px",
    color: "#111827",
    marginBottom: "8px",
  },
  subtitle: {
    fontSize: "16px",
    color: "#6b7280",
    maxWidth: "700px",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))",
    gap: "24px",
  },
  card: {
    backgroundColor: "#ffffff",
    borderRadius: "16px",
    padding: "24px",
    boxShadow: "0 10px 25px rgba(0, 0, 0, 0.08)",
  },
  cardTitle: {
    fontSize: "22px",
    color: "#111827",
    marginBottom: "20px",
  },
  tableContainer: {
    overflowX: "auto",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
  },
  th: {
    textAlign: "left",
    padding: "12px",
    backgroundColor: "#f9fafb",
    color: "#374151",
    fontSize: "14px",
    borderBottom: "1px solid #e5e7eb",
  },
  td: {
    padding: "12px",
    color: "#4b5563",
    fontSize: "14px",
    borderBottom: "1px solid #e5e7eb",
  },
  badge: {
    padding: "6px 10px",
    borderRadius: "999px",
    fontSize: "12px",
    fontWeight: "bold",
    textTransform: "capitalize",
  },
  list: {
    display: "flex",
    flexDirection: "column",
    gap: "16px",
  },
  listItem: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: "16px",
    padding: "16px",
    border: "1px solid #e5e7eb",
    borderRadius: "12px",
    backgroundColor: "#f9fafb",
  },
  itemTitle: {
    fontSize: "16px",
    margin: "0 0 6px 0",
    color: "#111827",
  },
  itemText: {
    fontSize: "14px",
    color: "#6b7280",
    margin: "4px 0",
  },
  primaryButton: {
    backgroundColor: "#2563eb",
    color: "#ffffff",
    border: "none",
    borderRadius: "8px",
    padding: "10px 14px",
    cursor: "pointer",
    fontWeight: "bold",
  },
  dangerButton: {
    backgroundColor: "#dc2626",
    color: "#ffffff",
    border: "none",
    borderRadius: "8px",
    padding: "10px 14px",
    cursor: "pointer",
    fontWeight: "bold",
  },
};

export default AdminPage;