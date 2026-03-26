import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { User } from "lucide-react";

export default function LoginSelection() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    correo: "",
    contrasena: "",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    console.log("Login unificado:", formData);

   
    navigate("/buscar");
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
                Iniciar sesión
              </CardTitle>

              <CardDescription className="text-center text-[#5F6670] text-xl leading-relaxed">
                Accede a tu cuenta
              </CardDescription>
            </CardHeader>

            <CardContent>
              <form onSubmit={handleSubmit} className="space-y-6">
                <div className="space-y-2">
                  <Label
                    htmlFor="correo"
                    className="text-[#0A0A0A] text-lg font-semibold"
                  >
                    Correo electrónico
                  </Label>
                  <Input
                    id="correo"
                    type="email"
                    placeholder="Ingresa tu correo"
                    value={formData.correo}
                    onChange={(e) =>
                      setFormData({ ...formData, correo: e.target.value })
                    }
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <div className="space-y-2">
                  <div className="flex justify-between items-center gap-4">
                    <Label
                      htmlFor="contrasena"
                      className="text-[#0A0A0A] text-lg font-semibold"
                    >
                      Contraseña
                    </Label>
                    <a
                      href="#"
                      className="text-sm text-[#0A0A0A] font-semibold hover:underline"
                    >
                      ¿Ha olvidado su contraseña?
                    </a>
                  </div>

                  <Input
                    id="contrasena"
                    type="password"
                    placeholder="Tu contraseña"
                    value={formData.contrasena}
                    onChange={(e) =>
                      setFormData({ ...formData, contrasena: e.target.value })
                    }
                    required
                    className="h-14 rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] text-[#0A0A0A] placeholder:text-[#6B7280]"
                  />
                </div>

                <Button
                  type="submit"
                  className="w-full h-14 rounded-2xl bg-gradient-to-r from-[#111111] to-[#2B2B2B] text-white text-lg font-semibold hover:opacity-90"
                >
                  Iniciar sesión
                </Button>

                <div className="text-center pt-6 border-t border-[#E5E7EB]">
                  <p className="text-[#5F6670]">
                    ¿No tienes una cuenta?{" "}
                    <Link
                      to="/registro"
                      className="text-[#0A0A0A] hover:underline font-semibold"
                    >
                      Regístrate
                    </Link>
                  </p>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}