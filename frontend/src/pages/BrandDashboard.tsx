import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { Card, CardContent } from "../components/ui/card";
import { Button } from "../components/ui/button";

import { useAuth } from "../context/AuthContext";
import { getBrandProfile, getMyProducts } from "../services/brandService";
import { 
  Building2, 
  Package, 
  Plus, 
  Users, 
  Star, 
  CheckCircle2, 
  ExternalLink,
  Loader2,
  Edit,
  Trash2
} from "lucide-react";
import { useNavigate } from "react-router-dom";

export default function BrandDashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  // Queries
  const { data: profile, isLoading: isProfileLoading } = useQuery({
    queryKey: ["brandProfile"],
    queryFn: () => getBrandProfile(user?.token || ""),
    enabled: !!user?.token,
  });

  const { data: products, isLoading: isProductsLoading } = useQuery({
    queryKey: ["brandProducts"],
    queryFn: () => getMyProducts(user?.token || ""),
    enabled: !!user?.token,
  });

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
    }
  };

  if (isProfileLoading || isProductsLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-[#F7F7F8]">
        <Loader2 className="h-10 w-10 animate-spin text-[#0A0A0A]" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto space-y-8">
          
          {/* Header de la Marca */}
          <section className="relative">
            <Card className="rounded-[40px] border-[#E5E7EB] overflow-hidden shadow-xl bg-white">
              <div className="h-32 bg-gradient-to-r from-[#0A0A0A] to-[#2B2B2B]" />
              <CardContent className="px-10 pb-10">
                <div className="flex flex-col md:flex-row items-end gap-6 -mt-16">
                  <div className="relative">
                    <div className="h-32 w-32 rounded-[32px] bg-white border-4 border-white shadow-lg flex items-center justify-center overflow-hidden">
                      {profile?.pictureUrl ? (
                        <img src={profile.pictureUrl} alt={profile.name} className="h-full w-full object-cover" />
                      ) : (
                        <Building2 className="h-12 w-12 text-[#9CA3AF]" />
                      )}
                    </div>
                    {profile?.verified && (
                      <div className="absolute -bottom-2 -right-2 bg-blue-500 text-white p-1.5 rounded-full border-4 border-white">
                        <CheckCircle2 className="h-5 w-5" />
                      </div>
                    )}
                  </div>
                  
                  <div className="flex-1 space-y-2 text-center md:text-left">
                    <div className="flex flex-col md:flex-row md:items-center gap-2 md:gap-4">
                      <h1 className="text-4xl font-black tracking-tight text-[#0A0A0A]">{profile?.name}</h1>
                    </div>
                    <div className="flex flex-wrap justify-center md:justify-start gap-4">
                      <a 
                        href={profile?.linkOfficial} 
                        target="_blank" 
                        rel="noopener noreferrer"
                        className="flex items-center gap-1 text-sm font-bold text-[#5F6670] hover:text-[#0A0A0A] transition-colors"
                      >
                        <ExternalLink className="h-4 w-4" />
                        Sitio Oficial
                      </a>
                    </div>
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline" className="rounded-xl font-bold">Editar Perfil</Button>
                    <Button onClick={handleLogout} variant="ghost" className="text-red-500 hover:text-red-600 font-bold">Salir</Button>
                  </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-10 pt-10 border-t border-[#E5E7EB]">
                  <div className="text-center md:text-left">
                    <div className="flex items-center justify-center md:justify-start gap-2 text-[#5F6670] mb-1">
                      <Users className="h-4 w-4" />
                      <span className="text-xs font-black uppercase tracking-widest">Seguidores</span>
                    </div>
                    <p className="text-2xl font-black text-[#0A0A0A]">{profile?.followersCount || 0}</p>
                  </div>
                  <div className="text-center md:text-left">
                    <div className="flex items-center justify-center md:justify-start gap-2 text-[#5F6670] mb-1">
                      <Star className="h-4 w-4" />
                      <span className="text-xs font-black uppercase tracking-widest">Calificación</span>
                    </div>
                    <p className="text-2xl font-black text-[#0A0A0A]">{profile?.rating || "0.0"}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          </section>

          {/* Gestión de Productos */}
          <section className="space-y-6">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-2xl font-black tracking-tight text-[#0A0A0A]">Mis Productos</h2>
                <p className="text-[#5F6670] font-semibold text-sm">Gestiona el inventario de tu marca</p>
              </div>
              <Button className="rounded-2xl h-12 px-6 flex items-center gap-2">
                <Plus className="h-5 w-5" />
                Agregar Producto
              </Button>
            </div>

            {products && products.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {products.map((p) => (
                  <Card key={p.id} className="group rounded-[32px] overflow-hidden border-[#E5E7EB] hover:shadow-xl transition-all">
                    <div className="aspect-square bg-[#F3F4F6] relative">
                      {p.pictureUrl ? (
                         <img src={p.pictureUrl} className="h-full w-full object-cover" />
                      ) : (
                        <Package className="h-10 w-10 text-[#9CA3AF] absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2" />
                      )}
                    </div>
                    <CardContent className="p-6 space-y-4">
                      <div className="flex justify-between items-start">
                        <h3 className="font-bold text-[#0A0A0A]">{p.name}</h3>
                        <span className="text-lg font-black">${p.price.toLocaleString('es-CO')}</span>
                      </div>
                      <div className="flex gap-2">
                        <Button variant="outline" size="sm" className="flex-1 rounded-xl">
                          <Edit className="h-4 w-4 mr-2" />
                          Editar
                        </Button>
                        <Button variant="ghost" size="sm" className="text-red-500 hover:bg-red-50 rounded-xl">
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            ) : (
              <Card className="rounded-[40px] border-dashed border-2 py-20 text-center">
                <Package className="h-12 w-12 text-[#9CA3AF] mx-auto mb-4" />
                <h3 className="text-xl font-bold">Aún no tienes productos registrados</h3>
                <p className="text-[#5F6670] mb-8">Empieza publicando tu primer diseño en Fashtoll.</p>
                <Button className="rounded-2xl">Crear mi primer producto</Button>
              </Card>
            )}
          </section>

        </div>
      </div>
    </div>
  );
}
