import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/card";
import { LoginForm } from "../components/auth/LoginForm";
import { Link } from "react-router-dom";
import { LogIn } from "lucide-react";

export default function Login() {
  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-40 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-md mx-auto">
          <Card className="rounded-[32px] overflow-hidden border-[#E5E7EB] shadow-2xl animate-fade-in">
            <CardHeader className="text-center pt-10">
              <div className="flex justify-center mb-6">
                <div className="p-4 rounded-2xl bg-[#EEF0F3] text-[#0A0A0A]">
                  <LogIn className="h-8 w-8" />
                </div>
              </div>
              <CardTitle className="text-4xl font-black tracking-tight">Bienvenido</CardTitle>
              <CardDescription className="text-lg">Accede a tu cuenta de Fashtoll</CardDescription>
            </CardHeader>

            <CardContent className="px-10 pb-10">
              <LoginForm />

              <div className="mt-8 pt-8 border-t border-[#E5E7EB] text-center space-y-4">
                <p className="text-[#5F6670]">
                  ¿No tienes cuenta?{" "}
                  <Link to="/registro" className="text-[#0A0A0A] font-bold hover:underline">
                    Regístrate ahora
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
