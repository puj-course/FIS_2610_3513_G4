import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { useAuth } from "../context/AuthContext";
import { User, Mail, Shield, LogOut } from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function ClientProfile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
    }
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-[#F7F7F8]">
        <Card className="p-8 text-center space-y-4">
          <p className="font-bold">No has iniciado sesión</p>
          <Button onClick={() => navigate("/login")}>Ir al Login</Button>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto">
          <div className="flex flex-col md:flex-row gap-8">
            {/* Sidebar de Perfil */}
            <aside className="w-full md:w-64 space-y-4">
              <Card className="p-6 text-center">
                <div className="mx-auto h-20 w-20 rounded-full bg-[#EEF0F3] flex items-center justify-center mb-4">
                  <User className="h-10 w-10 text-[#0A0A0A]" />
                </div>
                <h2 className="font-black text-xl tracking-tight">{user.email.split('@')[0]}</h2>
                <p className="text-xs font-bold text-[#5F6670] uppercase tracking-widest mt-1">Cliente VIP</p>
              </Card>

              <Button 
                variant="ghost" 
                className="w-full justify-start text-red-600 hover:bg-red-50 hover:text-red-700"
                onClick={handleLogout}
              >
                <LogOut className="mr-2 h-5 w-5" />
                Cerrar Sesión
              </Button>
            </aside>

            {/* Contenido Principal */}
            <main className="flex-1 space-y-6">
              <Card className="rounded-[32px] border-[#E5E7EB]">
                <CardHeader>
                  <CardTitle className="text-3xl font-black tracking-tight">Mi Perfil</CardTitle>
                  <CardDescription>Gestiona tu información personal y preferencias</CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-1">
                      <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest flex items-center gap-2">
                        <Mail className="h-3 w-3" />
                        Correo Electrónico
                      </label>
                      <p className="font-bold text-[#0A0A0A]">{user.email}</p>
                    </div>
                    <div className="space-y-1">
                      <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest flex items-center gap-2">
                        <Shield className="h-3 w-3" />
                        Rol de Usuario
                      </label>
                      <p className="font-bold text-[#0A0A0A]">{user.role}</p>
                    </div>
                  </div>

                  <div className="pt-6 border-t border-[#E5E7EB]">
                      <Button variant="outline" className="rounded-xl">
                          Editar Información
                      </Button>
                  </div>
                </CardContent>
              </Card>

              {/* Sección de Actividad (Simulada para MVP) */}
              <Card className="rounded-[32px] border-[#E5E7EB] bg-[#F9FAFB]/50 border-dashed">
                <CardContent className="py-20 text-center space-y-4">
                  <div className="p-3 bg-white rounded-2xl inline-block shadow-sm">
                    <Star className="h-6 w-6 text-yellow-500 fill-current" />
                  </div>
                  <h3 className="font-bold text-[#0A0A0A]">Tus Favoritos aparecerán aquí</h3>
                  <p className="text-sm text-[#5F6670] max-w-xs mx-auto">
                    Guarda las prendas que más te gusten para verlas más tarde en tu perfil personal.
                  </p>
                </CardContent>
              </Card>
            </main>
          </div>
        </div>
      </div>
    </div>
  );
}

// Reutilizamos el icon de Star
function Star({ className }: { className?: string }) {
    return (
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className={className}><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
    )
}
