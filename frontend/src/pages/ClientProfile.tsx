import { useState } from "react";
import { Navbar } from "../components/Navbar";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "../components/ui/card";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { useAuth } from "../hooks/useAuth";
import { User, Mail, LogOut, Heart, Store, Lock, Save, Loader2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import * as clientService from "../services/clientService";
import { useQuery, useMutation } from "@tanstack/react-query";

type Section = "profile" | "favorites" | "brands" | "password";

export default function ClientProfile() {
  const { user, logout, updateUserName } = useAuth();
  const navigate = useNavigate();
  const [activeSection, setActiveSection] = useState<Section>("profile");

  // State for forms
  const [name, setName] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState<{ type: "success" | "error"; text: string } | null>(null);

  // Fetch profile data
  const { data: profile, isLoading: isProfileLoading, refetch: refetchProfile } = useQuery({
    queryKey: ["clientProfile"],
    queryFn: () => clientService.getProfile(user?.token || ""),
    enabled: !!user?.token,
  });

  // Sync name from profile data using "set during render" pattern
  const [prevProfileName, setPrevProfileName] = useState<string | null>(null);
  if (profile?.name && profile.name !== prevProfileName) {
    setPrevProfileName(profile.name);
    setName(profile.name);
    if (user && user.name !== profile.name) {
      updateUserName(profile.name);
    }
  }

  // Mutations
  const updateNameMutation = useMutation({
    mutationFn: (newName: string) => clientService.updateProfile(user?.token || "", { name: newName }),
    onSuccess: (data) => {
      setMessage({ type: "success", text: "Nombre actualizado correctamente" });
      updateUserName(data.name);
      refetchProfile();
      setTimeout(() => setMessage(null), 3000);
    },
    onError: () => {
      setMessage({ type: "error", text: "Error al actualizar el nombre" });
      setTimeout(() => setMessage(null), 3000);
    }
  });

  const changePasswordMutation = useMutation({
    mutationFn: () => clientService.changePassword(user?.token || "", { currentPassword, newPassword }),
    onSuccess: () => {
      setMessage({ type: "success", text: "Contraseña cambiada correctamente" });
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
      setTimeout(() => setMessage(null), 3000);
    },
    onError: (error: unknown) => {
      const errorMsg = (error as {response?: {data?: {message?: string}}})?.response?.data?.message || "Error al cambiar la contraseña";
      setMessage({ type: "error", text: errorMsg });
      setTimeout(() => setMessage(null), 3000);
    }
  });

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
    }
  };

  const handleUpdateName = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    updateNameMutation.mutate(name);
  };

  const handleChangePassword = (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      setMessage({ type: "error", text: "Las contraseñas no coinciden" });
      return;
    }
    if (newPassword.length < 6) {
        setMessage({ type: "error", text: "La nueva contraseña debe tener al menos 6 caracteres" });
        return;
    }
    changePasswordMutation.mutate();
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

  const renderSection = () => {
    switch (activeSection) {
      case "profile":
        return (
          <Card className="rounded-[32px] border-[#E5E7EB]">
            <CardHeader>
              <CardTitle className="text-3xl font-black tracking-tight">Mi Perfil</CardTitle>
              <CardDescription>Gestiona tu información personal</CardDescription>
            </CardHeader>
            <CardContent className="space-y-8">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div className="space-y-1">
                  <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest flex items-center gap-2">
                    <Mail className="h-3 w-3" />
                    Correo Electrónico
                  </label>
                  <p className="font-bold text-[#0A0A0A] text-lg">{user.email}</p>
                </div>
              </div>

              <div className="pt-8 border-t border-[#F3F4F6]">
                <form onSubmit={handleUpdateName} className="space-y-4 max-w-md">
                  <div className="space-y-2">
                    <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest">
                      Nombre Completo
                    </label>
                    <Input 
                      value={name} 
                      onChange={(e) => setName(e.target.value)}
                      placeholder="Tu nombre"
                      className="rounded-xl border-[#E5E7EB] font-medium"
                    />
                  </div>
                  <Button 
                    type="submit" 
                    className="rounded-xl px-8"
                    disabled={updateNameMutation.isPending || name === profile?.name}
                  >
                    {updateNameMutation.isPending ? (
                      <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    ) : (
                      <Save className="mr-2 h-4 w-4" />
                    )}
                    Guardar Cambios
                  </Button>
                </form>
              </div>
            </CardContent>
          </Card>
        );
      case "favorites":
        return (
          <Card className="rounded-[32px] border-[#E5E7EB] bg-[#F9FAFB]/50 border-dashed">
            <CardContent className="py-24 text-center space-y-4">
              <div className="p-4 bg-white rounded-2xl inline-block shadow-sm">
                <Heart className="h-8 w-8 text-red-500 fill-current" />
              </div>
              <h3 className="font-black text-2xl text-[#0A0A0A]">Tus Favoritos aparecerán aquí</h3>
              <p className="text-base text-[#5F6670] max-w-sm mx-auto">
                Guarda las prendas que más te gusten para verlas más tarde en tu perfil personal.
              </p>
            </CardContent>
          </Card>
        );
      case "brands":
        return (
          <Card className="rounded-[32px] border-[#E5E7EB] bg-[#F9FAFB]/50 border-dashed">
            <CardContent className="py-24 text-center space-y-4">
              <div className="p-4 bg-white rounded-2xl inline-block shadow-sm">
                <Store className="h-8 w-8 text-blue-500" />
              </div>
              <h3 className="font-black text-2xl text-[#0A0A0A]">Marcas que sigues</h3>
              <p className="text-base text-[#5F6670] max-w-sm mx-auto">
                Aquí podrás ver las actualizaciones y nuevos lanzamientos de tus marcas favoritas.
              </p>
            </CardContent>
          </Card>
        );
      case "password":
        return (
          <Card className="rounded-[32px] border-[#E5E7EB]">
            <CardHeader>
              <CardTitle className="text-3xl font-black tracking-tight">Cambiar Contraseña</CardTitle>
              <CardDescription>Actualiza tu seguridad regularmente</CardDescription>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleChangePassword} className="space-y-6 max-w-md">
                <div className="space-y-2">
                  <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest">
                    Contraseña Actual
                  </label>
                  <Input 
                    type="password"
                    value={currentPassword}
                    onChange={(e) => setCurrentPassword(e.target.value)}
                    className="rounded-xl border-[#E5E7EB]"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest">
                    Nueva Contraseña
                  </label>
                  <Input 
                    type="password"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    className="rounded-xl border-[#E5E7EB]"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <label className="text-xs font-black uppercase text-[#9CA3AF] tracking-widest">
                    Confirmar Nueva Contraseña
                  </label>
                  <Input 
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    className="rounded-xl border-[#E5E7EB]"
                    required
                  />
                </div>
                <Button 
                  type="submit" 
                  className="w-full rounded-xl"
                  disabled={changePasswordMutation.isPending}
                >
                  {changePasswordMutation.isPending ? (
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  ) : (
                    <Lock className="mr-2 h-4 w-4" />
                  )}
                  Actualizar Contraseña
                </Button>
              </form>
            </CardContent>
          </Card>
        );
    }
  };

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <div className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-6xl mx-auto">
          <div className="flex flex-col md:flex-row gap-8">
            {/* Sidebar */}
            <aside className="w-full md:w-72 space-y-6">
              <Card className="p-8 text-center overflow-hidden relative">
                <div className="mx-auto h-24 w-24 rounded-full bg-[#EEF0F3] flex items-center justify-center mb-4 border-4 border-white shadow-sm">
                  <User className="h-12 w-12 text-[#0A0A0A]" />
                </div>
                <h2 className="font-black text-2xl tracking-tight text-[#0A0A0A] truncate">
                  {profile?.name || user.email.split('@')[0]}
                </h2>
                <p className="text-xs font-bold text-[#5F6670] uppercase tracking-widest mt-1">
                  Cliente de Fashtoll
                </p>
              </Card>

              <nav className="flex flex-col gap-2">
                <SidebarButton 
                  active={activeSection === "profile"} 
                  onClick={() => setActiveSection("profile")}
                  icon={<User className="h-5 w-5" />}
                  label="Mi Perfil"
                />
                <SidebarButton 
                  active={activeSection === "favorites"} 
                  onClick={() => setActiveSection("favorites")}
                  icon={<Heart className="h-5 w-5" />}
                  label="Mis Favoritos"
                />
                <SidebarButton 
                  active={activeSection === "brands"} 
                  onClick={() => setActiveSection("brands")}
                  icon={<Store className="h-5 w-5" />}
                  label="Marcas que Sigo"
                />
                <SidebarButton 
                  active={activeSection === "password"} 
                  onClick={() => setActiveSection("password")}
                  icon={<Lock className="h-5 w-5" />}
                  label="Cambiar Contraseña"
                />
                
                <div className="pt-4 mt-4 border-t border-[#E5E7EB]">
                  <Button 
                    variant="ghost" 
                    className="w-full justify-start text-red-600 hover:bg-red-50 hover:text-red-700 rounded-xl h-12 font-bold"
                    onClick={handleLogout}
                  >
                    <LogOut className="mr-3 h-5 w-5" />
                    Cerrar Sesión
                  </Button>
                </div>
              </nav>
            </aside>

            {/* Contenido Principal */}
            <main className="flex-1">
              {message && (
                <div className={`mb-6 p-4 rounded-2xl font-bold flex items-center gap-3 animate-in fade-in slide-in-from-top-4 duration-300 ${
                  message.type === "success" ? "bg-green-50 text-green-700 border border-green-100" : "bg-red-50 text-red-700 border border-red-100"
                }`}>
                  <div className={`h-2 w-2 rounded-full ${message.type === "success" ? "bg-green-500" : "bg-red-500"}`} />
                  {message.text}
                </div>
              )}
              {isProfileLoading ? (
                <div className="flex h-64 items-center justify-center">
                  <Loader2 className="h-8 w-8 animate-spin text-[#5F6670]" />
                </div>
              ) : (
                renderSection()
              )}
            </main>
          </div>
        </div>
      </div>
    </div>
  );
}

function SidebarButton({ active, onClick, icon, label }: { active: boolean; onClick: () => void; icon: React.ReactNode; label: string }) {
  return (
    <Button
      variant={active ? "default" : "ghost"}
      className={`w-full justify-start rounded-xl h-12 font-bold transition-all duration-200 ${
        active 
          ? "bg-[#0A0A0A] text-white shadow-lg shadow-black/10 scale-[1.02]" 
          : "text-[#5F6670] hover:bg-[#F3F4F6] hover:text-[#0A0A0A]"
      }`}
      onClick={onClick}
    >
      <span className="mr-3">{icon}</span>
      {label}
    </Button>
  );
}
