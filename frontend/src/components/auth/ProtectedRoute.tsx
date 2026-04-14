import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: string[];
}

/**
 * Componente para proteger rutas privadas.
 * Verifica si hay un usuario autenticado y si tiene el rol adecuado.
 */
export function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  // Si aún se está cargando la sesión (ej. restaurando de localStorage), no mostrar nada
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[300px]">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-gray-900 border-t-transparent"></div>
      </div>
    );
  }

  // Si no hay usuario autenticado, redirigir al Login
  if (!user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Si hay roles permitidos y el usuario no tiene uno de ellos
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    // Redirección dinámica según el rol para evitar bucles
    const redirectPath = user.role === "BRAND" ? "/perfil-marca" : "/perfil";
    return <Navigate to={redirectPath} replace />;
  }

  return <>{children}</>;
}
