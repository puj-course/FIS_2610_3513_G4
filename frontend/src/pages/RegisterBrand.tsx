import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Building2 } from "lucide-react";

export default function RegisterBrand() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    nombre: "",
    correo: "",
    enlace: "",
    foto: "",
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

    const marcaData = {
      nombre: formData.nombre,
      correo: formData.correo,
      contrasena: formData.contrasena,
      foto: formData.foto,
      enlace: formData.enlace,
      seguidores: 0,
      calificacion: 0,
      estaVerificada: false,
    };

    console.log("Marca registration:", marcaData);

    navigate("/perfil-marca");
  };

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-36 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <Card className="rounded-[28px] border border-[#E5E7EB] bg-white shadow-md">
            <CardHeader>
              <div className="rounded-full w-20 h-20 bg-[#EEF0F3] flex items-center justify-center mb-6 mx-auto">
                <Building2 className="h-9 w-9 text-[#8F98A3]" />
              </div>

              <CardTitle className="text-center text-4xl font-bold text-[#0A0A0A]">
                Registro de Marca
              </CardTitle>

              <CardDescription className="text-center text-[#5F6670] text-xl leading-relaxed">
                Registra tu marca para empezar a mostrar tus productos
              </CardDescription>
            </CardHeader>

            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="nombre" className="text-[#0A0A0A] text-lg font-semibold">
                    Nombre de la marca
                  </Label>
                  <Input
                    id="nombre"
                    type="text"
                    placeholder="Nombre de tu marca"
                    value={formData.nombre}
                    onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="correo" className="text-[#0A0A0A] text-lg font-semibold">
                    Correo electrónico
                  </Label>
                  <Input
                    id="correo"
                    type="email"
                    placeholder="marca@email.com"
                    value={formData.correo}
                    onChange={(e) => setFormData({ ...formData, correo: e.target.value })}
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="enlace" className="text-[#0A0A0A] text-lg font-semibold">
                    Enlace de la marca
                  </Label>
                  <Input
                    id="enlace"
                    type="url"
                    placeholder="https://tumarca.com"
                    value={formData.enlace}
                    onChange={(e) => setFormData({ ...formData, enlace: e.target.value })}
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <div className="space-y-2">
                  <Label htmlFor="foto" className="text-[#0A0A0A] text-lg font-semibold">
                    Foto o logo de la marca
                  </Label>
                  <Input
                    id="foto"
                    type="text"
                    placeholder="URL de la imagen o logo"
                    value={formData.foto}
                    onChange={(e) => setFormData({ ...formData, foto: e.target.value })}
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <div className="grid md:grid-cols-2 gap-4">
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
                      className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
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
                      className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                    />
                  </div>
                </div>

                <Button
                  type="submit"
                  className="w-full h-14 rounded-2xl bg-gradient-to-r from-[#111111] to-[#2B2B2B] text-white text-lg font-semibold hover:opacity-90"
                >
                  Crear cuenta de marca
                </Button>

                <div className="text-center text-sm text-[#5F6670] leading-relaxed">
                  Al registrarte, aceptas nuestros{" "}
                  <a href="#" className="text-[#0A0A0A] font-semibold hover:underline">
                    Términos y Condiciones para Marcas
                  </a>
                </div>

                <div className="text-center pt-6 border-t border-[#E5E7EB]">
                  <p className="text-[#5F6670]">
                    ¿Ya tienes una cuenta?{" "}
                    <Link to="/login/marca" className="text-[#0A0A0A] hover:underline font-semibold">
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