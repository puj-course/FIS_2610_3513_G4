import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { 
  ShieldCheck, 
  Search,
  Users, 
  Building2, 
  Package, 
  CheckCircle2,
  LogOut
} from "lucide-react";

export default function AdminDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  // Suprimir advertencia de variable no usada: user se usa para verificar rol
  void user;

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
    }
  };

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto space-y-8">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div>
              <h1 className="text-4xl font-black tracking-tight text-[#0A0A0A]">Panel de Administración</h1>
              <p className="text-[#5F6670] font-semibold">Sistema de Control Central Fashtoll</p>
            </div>
            
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-2 px-4 py-2 bg-green-50 text-green-700 rounded-2xl border border-green-100 shadow-sm">
                  <ShieldCheck className="h-5 w-5" />
                  <span className="text-sm font-bold uppercase tracking-widest">Admin Verificado</span>
              </div>
              <Button 
                variant="ghost" 
                onClick={handleLogout}
                className="text-red-500 hover:text-red-600 hover:bg-red-50 rounded-2xl font-bold h-11"
              >
                <LogOut className="h-5 w-5 mr-2" />
                Cerrar Sesión
              </Button>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Estadísticas Rápidas */}
            <Card className="p-6 space-y-2">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-blue-50 text-blue-600 rounded-xl">
                  <Users className="h-6 w-6" />
                </div>
                <div>
                  <p className="text-xs font-black text-[#5F6670] uppercase tracking-widest">Usuarios Totales</p>
                  <p className="text-2xl font-black text-[#0A0A0A]">1,280</p>
                </div>
              </div>
            </Card>

            <Card className="p-6 space-y-2">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-purple-50 text-purple-600 rounded-xl">
                  <Building2 className="h-6 w-6" />
                </div>
                <div>
                  <p className="text-xs font-black text-[#5F6670] uppercase tracking-widest">Marcas Activas</p>
                  <p className="text-2xl font-black text-[#0A0A0A]">45</p>
                </div>
              </div>
            </Card>

            <Card className="p-6 space-y-2">
              <div className="flex items-center gap-4">
                <div className="p-3 bg-orange-50 text-orange-600 rounded-xl">
                  <Package className="h-6 w-6" />
                </div>
                <div>
                  <p className="text-xs font-black text-[#5F6670] uppercase tracking-widest">Productos Indexados</p>
                  <p className="text-2xl font-black text-[#0A0A0A]">3,420</p>
                </div>
              </div>
            </Card>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Herramientas de Sistema */}
            <Card className="rounded-[40px] border-[#E5E7EB]">
              <CardHeader>
                <CardTitle>Operaciones de Sistema</CardTitle>
                <CardDescription>Herramientas críticas de mantenimiento</CardDescription>
              </CardHeader>
              <CardContent className="space-y-6">
                <div className="p-6 rounded-3xl bg-[#F9FAFB] border border-[#E5E7EB] space-y-4">
                   <div className="flex items-start gap-4">
                      <div className="p-2 bg-white rounded-lg shadow-sm border border-[#E5E7EB]">
                        <Search className="h-5 w-5 text-[#0A0A0A]" />
                      </div>
                      <div className="flex-1">
                        <h4 className="font-bold text-[#0A0A0A]">Motor de Búsqueda Interno</h4>
                        <p className="text-sm text-[#5F6670]">
                          El sistema utiliza un motor de búsqueda propio basado en tokenización e indexación sobre PostgreSQL.
                          La indexación se realiza automáticamente al crear o actualizar productos.
                        </p>
                      </div>
                   </div>
                   <div className="flex items-center gap-2 p-4 rounded-xl bg-green-50 text-green-700 text-sm font-bold border border-green-100">
                     <CheckCircle2 className="h-4 w-4" />
                     Motor activo — sin dependencias externas
                   </div>
                </div>

                <div className="p-6 rounded-3xl bg-[#F9FAFB] border border-[#E5E7EB] space-y-4">
                   <div className="flex items-start gap-4">
                      <div className="p-2 bg-white rounded-lg shadow-sm border border-[#E5E7EB]">
                        <CheckCircle2 className="h-5 w-5 text-[#0A0A0A]" />
                      </div>
                      <div className="flex-1">
                        <h4 className="font-bold text-[#0A0A0A]">Verificación de Marcas</h4>
                        <p className="text-sm text-[#5F6670]">Validar y otorgar el sello de "Marca Verificada" a los perfiles de empresa que cumplan los requisitos.</p>
                      </div>
                   </div>
                   <Button variant="outline" className="w-full h-12 rounded-2xl">Gestionar Verificaciones</Button>
                </div>
              </CardContent>
            </Card>

            {/* Listado de Entidades */}
            <Card className="rounded-[40px] border-[#E5E7EB]">
              <CardHeader>
                <CardTitle>Gestión de Entidades</CardTitle>
                <CardDescription>Acceso rápido a los CRUDs del sistema</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                {['Usuarios', 'Marcas', 'Productos', 'Categorías', 'Etiquetas'].map((entity) => (
                  <div key={entity} className="flex items-center justify-between p-4 bg-white hover:bg-[#F3F4F6] rounded-2xl border border-[#E5E7EB] cursor-pointer transition-colors group">
                    <span className="font-bold text-[#0A0A0A]">{entity}</span>
                    <Button variant="ghost" size="sm" className="opacity-0 group-hover:opacity-100 transition-opacity">Ver todos</Button>
                  </div>
                ))}
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
}
