import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Registration from "./pages/Registration";
import Catalog from "./pages/Catalog";
import ProductDetail from "./pages/ProductDetail";
import BrandListing from "./pages/BrandListing";
import BrandPublicProfile from "./pages/BrandPublicProfile";
import ClientProfile from "./pages/ClientProfile";
import BrandDashboard from "./pages/BrandDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import AboutUs from "./pages/AboutUs";

import { ProtectedRoute } from "./components/auth/ProtectedRoute";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registro" element={<Registration />} />
            <Route path="/productos" element={<Catalog />} />
            <Route path="/productos/:id" element={<ProductDetail />} />
            <Route path="/marcas" element={<BrandListing />} />
            <Route path="/marcas/:id" element={<BrandPublicProfile />} />
            <Route path="/sobre-nosotros" element={<AboutUs />} />
            
            {/* Rutas Protegidas */}
            <Route 
              path="/perfil" 
              element={
                <ProtectedRoute allowedRoles={["CLIENT"]}>
                  <ClientProfile />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/perfil-marca" 
              element={
                <ProtectedRoute allowedRoles={["BRAND"]}>
                  <BrandDashboard />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/admin" 
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <AdminDashboard />
                </ProtectedRoute>
              } 
            />
          </Routes>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
