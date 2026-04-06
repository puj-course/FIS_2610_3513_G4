import { useState } from "react";
import { Link } from "react-router-dom";
import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { RegisterFormClient } from "../components/auth/RegisterFormClient";

import { RegisterFormBrand } from "../components/auth/RegisterFormBrand";
import { User, Building2 } from "lucide-react";

export default function Registration() {
  const [type, setType] = useState<"CLIENT" | "BRAND">("CLIENT");

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-xl mx-auto">
          <Card className="rounded-[32px] overflow-hidden border-[#E5E7EB] shadow-xl">
            <CardHeader className="text-center pt-10">
              <div className="flex justify-center mb-6">
                <div className="p-4 rounded-2xl bg-[#EEF0F3] text-[#0A0A0A]">
                  {type === "CLIENT" ? <User className="h-8 w-8" /> : <Building2 className="h-8 w-8" />}
                </div>
              </div>
              <CardTitle className="text-4xl font-black tracking-tight">
                {type === "CLIENT" ? "Registro de Cliente" : "Registro de Marca"}
              </CardTitle>
              <CardDescription className="text-lg">
                {type === "CLIENT" 
                  ? "Crea tu cuenta para empezar a buscar prendas" 
                  : "Registra tu marca para empezar a mostrar tus productos"
                }
              </CardDescription>
            </CardHeader>

            <CardContent className="px-10 pb-10">
              {/* Switcher */}
              <div className="flex p-1 bg-[#F3F4F6] rounded-2xl mb-10">
                <button
                  onClick={() => setType("CLIENT")}
                  className={`flex-1 flex items-center justify-center gap-2 py-3 rounded-xl text-sm font-bold transition-all ${
                    type === "CLIENT" 
                      ? "bg-white text-[#0A0A0A] shadow-sm" 
                      : "text-[#5F6670] hover:text-[#0A0A0A]"
                  }`}
                >
                  <User className="h-4 w-4" />
                  Soy Cliente
                </button>
                <button
                  onClick={() => setType("BRAND")}
                  className={`flex-1 flex items-center justify-center gap-2 py-3 rounded-xl text-sm font-bold transition-all ${
                    type === "BRAND" 
                      ? "bg-white text-[#0A0A0A] shadow-sm" 
                      : "text-[#5F6670] hover:text-[#0A0A0A]"
                  }`}
                >
                  <Building2 className="h-4 w-4" />
                  Soy Marca
                </button>
              </div>

              {/* Formularios */}
              {type === "CLIENT" ? <RegisterFormClient /> : <RegisterFormBrand />}

              <div className="mt-8 pt-8 border-t border-[#E5E7EB] text-center space-y-4">
                <p className="text-[#5F6670]">
                  ¿Ya tienes una cuenta?{" "}
                  <Link to="/login" className="text-[#0A0A0A] font-bold hover:underline">
                    Inicia Sesión
                  </Link>
                </p>
                <Link to="/" className="inline-block text-sm text-[#5F6670] hover:text-[#0A0A0A] transition-colors">
                  ← Volver al inicio
                </Link>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
