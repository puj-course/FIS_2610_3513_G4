import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { User } from "lucide-react";

export default function RegisterClient() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    nombre: "",
    correo: "",
    contrasena: "",
    confirmarContrasena: "",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (formData.contrasena !== formData.confirmarContrasena) {
      alert("Las contraseñas no coinciden");
      return;
    }

    if (formData.contrasena.length < 8) {
      alert("La contraseña debe tener mínimo 8 caracteres");
      return;
    }

    const clienteData = {
      nombre: formData.nombre,
      correo: formData.correo,
      contrasena: formData.contrasena,
    };

    console.log("Cliente registration:", clienteData);

    navigate("/perfil");
  };

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-36 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-xl mx-auto">
          <Card className="rounded-[28px] border border-[#E5E7EB] bg-white shadow-md">
            <CardHeader>
              <div className="rounded-full w-20 h-20 bg-[#EEF0F3] flex items-center justify-center mb-6 mx-auto">
                <User className="h-9 w-9 text-[#8F98A3]" />
              </div>

              <CardTitle className="text-center text-4xl font-bold text-[#0A0A0A]">
                Registro de Cliente
              </CardTitle>

              <CardDescription className="text-center text-[#5F6670] text-xl leading-relaxed">
                Crea tu cuenta para empezar a buscar prendas
              </CardDescription>
            </CardHeader>

            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="nombre" className="text-[#0A0A0A] text-lg font-semibold">
                    Nombre completo
                  </Label>
                  <Input
                    id="nombre"
                    type="text"
                    placeholder="Tu nombre"
                    value={formData.nombre}
                    onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280] focus-visible:ring-1 focus-visible:ring-[#AEB4BC]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="correo" className="text-[#0A0A0A] text-lg font-semibold">
                    Email
                  </Label>
                  <Input
                    id="correo"
                    type="email"
                    placeholder="tu@email.com"
                    value={formData.correo}
                    onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280] focus-visible:ring-1 focus-visible:ring-[#AEB4BC]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="contrasena" className="text-[#0A0A0A] text-lg font-semibold">
                    Contraseña
                  </Label>
                  <Input
                    id="contrasena"
                    type="password"
                    placeholder="Mínimo 8 caracteres"
                    value={formData.contrasena}
                    onChange={(e) => setFormData({ ...formData, contrasena: e.target.value })}
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280] focus-visible:ring-1 focus-visible:ring-[#AEB4BC]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="confirmarContrasena" className="text-[#0A0A0A] text-lg font-semibold">
                    Confirmar contraseña
                  </Label>
                  <Input
                    id="confirmarContrasena"
                    type="password"
                    placeholder="Repite tu contraseña"
                    value={formData.confirmarContrasena}
                    onChange={(e) =>
                      setFormData({ ...formData, confirmarContrasena: e.target.value })
                    }
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280] focus-visible:ring-1 focus-visible:ring-[#AEB4BC]"
                  />
                </div>

                <Button
                  type="submit"
                  className="w-full h-14 rounded-2xl bg-gradient-to-r from-[#111111] to-[#2B2B2B] text-white text-lg font-semibold hover:opacity-90"
                >
                  Crear cuenta
                </Button>

                <div className="text-center text-sm text-[#5F6670] leading-relaxed">
                  Al registrarte, aceptas nuestros{" "}
                  <a href="#" className="text-[#0A0A0A] font-semibold hover:underline">
                    Términos y Condiciones
                  </a>
                </div>

                <div className="text-center pt-6 border-t border-[#E5E7EB]">
                  <p className="text-[#5F6670]">
                    ¿Ya tienes una cuenta?{" "}
                    <Link
                      to="/login/cliente"
                      className="text-[#0A0A0A] hover:underline font-semibold"
                    >
                      Inicia sesión
                    </Link>
                  </p>
                </div>

                <div className="text-center">
                  <Link to="/registro" className="text-sm text-[#5F6670] hover:text-[#0A0A0A]">
                    ← Volver a selección
                  </Link>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}