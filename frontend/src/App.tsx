import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Registration from "./pages/Registration";
import Catalog from "./pages/Catalog";
import ClientProfile from "./pages/ClientProfile";
import BrandDashboard from "./pages/BrandDashboard";
import AdminDashboard from "./pages/AdminDashboard";



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
