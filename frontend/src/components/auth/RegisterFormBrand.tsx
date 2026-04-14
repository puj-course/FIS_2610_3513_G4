import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { Label } from "../ui/label";
import { Building2, Loader2, Link as LinkIcon } from "lucide-react";
import { register } from "../../services/authService";

import { useAuth } from "../../hooks/useAuth";

export function RegisterFormBrand() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const [formData, setFormData] = useState({
    nombre: "",
    email: "",
    linkOfficial: "",
    pictureUrl: "",
    password: "",
    confirmPassword: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validaciones básicas
    if (formData.password !== formData.confirmPassword) {
      setError("Las contraseñas no coinciden");
      return;
    }

    if (formData.password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres");
      return;
    }

    setLoading(true);
    try {
      const response = await register({
        email: formData.email,
        password: formData.password,
        name: formData.nombre,
        role: "BRAND",
        pictureUrl: formData.pictureUrl,
        linkOfficial: formData.linkOfficial,
      });

      // Auto-login
      login(response as any);
      navigate("/perfil-marca");
    } catch (err: unknown) {
      const errorMsg = (err as { response?: { data?: { message?: string } } }).response?.data?.message;
      setError(errorMsg || "Ocurrió un error en el registro de la marca");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6 animate-fade-in">
      {error && (
        <div className="p-4 rounded-xl bg-red-50 border border-red-200 text-red-600 text-sm font-semibold">
          {error}
        </div>
      )}

      <div className="space-y-2">
        <Label htmlFor="nombre">Nombre de la Marca</Label>
        <Input
          id="nombre"
          placeholder="Nombre oficial"
          value={formData.nombre}
          onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
          required
        />
      </div>

      <div className="space-y-2">
        <Label htmlFor="email">Email Corporativo</Label>
        <Input
          id="email"
          type="email"
          placeholder="marca@email.com"
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          required
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="linkOfficial">Sitio Web / Link Oficial</Label>
          <div className="relative">
            <Input
              id="linkOfficial"
              type="url"
              placeholder="https://tumarca.com"
              value={formData.linkOfficial}
              onChange={(e) => setFormData({ ...formData, linkOfficial: e.target.value })}
              className="pl-10"
            />
            <LinkIcon className="absolute left-3 top-3 h-5 w-5 text-[#5F6670]" />
          </div>
        </div>
        <div className="space-y-2">
          <Label htmlFor="pictureUrl">URL de Logo / Imagen</Label>
          <Input
            id="pictureUrl"
            placeholder="URL de la imagen"
            value={formData.pictureUrl}
            onChange={(e) => setFormData({ ...formData, pictureUrl: e.target.value })}
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="password">Contraseña</Label>
          <Input
            id="password"
            type="password"
            placeholder="••••••••"
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            required
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="confirmPassword">Confirmar Contraseña</Label>
          <Input
            id="confirmPassword"
            type="password"
            placeholder="••••••••"
            value={formData.confirmPassword}
            onChange={(e) => setFormData({ ...formData, confirmPassword: e.target.value })}
            required
          />
        </div>
      </div>

      <Button type="submit" size="lg" className="w-full" disabled={loading}>
        {loading ? (
          <>
            <Loader2 className="mr-2 h-5 w-5 animate-spin" />
            Registrando marca...
          </>
        ) : (
          <>
            <Building2 className="mr-2 h-5 w-5" />
            Crear cuenta de marca
          </>
        )}
      </Button>
    </form>
  );
}
