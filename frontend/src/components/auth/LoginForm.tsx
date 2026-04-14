import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Label } from "../ui/label";
import { LogIn, Loader2 } from "lucide-react";

import { login as loginService } from "../../services/authService";
import { useAuth } from "../../hooks/useAuth";
import type { AuthUser } from "../../context/AuthTypes";

export function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await loginService(formData);
      login(response as AuthUser);
      
      // Redirigir según el rol
      switch (response.role) {
        case "ADMIN":
          navigate("/admin");
          break;
        case "BRAND":
          navigate("/perfil-marca");
          break;
        case "CLIENT":
          navigate("/perfil");
          break;
        default:
          navigate("/");
      }
    } catch (err: unknown) {
      const errorMsg = (err as { response?: { data?: { message?: string } } }).response?.data?.message;
      setError(errorMsg || "Credenciales inválidas");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {error && (
        <div className="p-4 rounded-xl bg-red-50 border border-red-200 text-red-600 text-sm font-semibold">
          {error}
        </div>
      )}

      <div className="space-y-2">
        <Label htmlFor="email">Email</Label>
        <Input
          id="email"
          type="email"
          placeholder="tu@email.com"
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          required
        />
      </div>

      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <Label htmlFor="password">Contraseña</Label>
          <Link to="/recovery" className="text-sm font-bold text-[#5F6670] hover:text-[#0A0A0A] transition-colors">
            ¿Olvidaste tu contraseña?
          </Link>
        </div>
        <Input
          id="password"
          type="password"
          placeholder="••••••••"
          value={formData.password}
          onChange={(e) => setFormData({ ...formData, password: e.target.value })}
          required
        />
      </div>

      <Button type="submit" size="lg" className="w-full" disabled={loading}>
        {loading ? (
          <>
            <Loader2 className="mr-2 h-5 w-5 animate-spin" />
            Iniciando sesión...
          </>
        ) : (
          <>
            <LogIn className="mr-2 h-5 w-5" />
            Entrar a Fashtoll
          </>
        )}
      </Button>
    </form>
  );
}
