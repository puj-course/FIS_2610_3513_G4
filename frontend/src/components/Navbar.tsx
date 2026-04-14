import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Search, User, LogIn, UserPlus, LogOut, ChevronDown } from "lucide-react";
import { Button } from "./ui/button";
import { useAuth } from "../hooks/useAuth";

export function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    if (window.confirm("¿Estás seguro de que quieres cerrar sesión?")) {
      logout();
      navigate("/");
      setIsMenuOpen(false);
    }
  };

  const getProfilePath = () => {
    if (user?.role === "ADMIN") return "/admin";
    if (user?.role === "BRAND") return "/perfil-marca";
    return "/perfil";
  };

  const profilePath = getProfilePath();

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-white/80 backdrop-blur-md border-b border-[#E5E7EB]">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-20">
          {/* Logo */}
          <div className="flex-shrink-0">
            <Link to="/" className="text-3xl font-black tracking-tighter text-[#0A0A0A]">
              Fashtoll
            </Link>
          </div>

          {/* Enlaces principales */}
          <div className="hidden md:ml-10 md:flex md:space-x-8 items-center">
            <Link 
              to="/productos" 
              className="text-[#5F6670] hover:text-[#0A0A0A] font-semibold transition-colors"
            >
              Productos
            </Link>
            <Link 
              to="/marcas" 
              className="text-[#5F6670] hover:text-[#0A0A0A] font-semibold transition-colors"
            >
              Marcas
            </Link>
            <Link 
              to="/sobre-nosotros" 
              className="text-[#5F6670] hover:text-[#0A0A0A] font-semibold transition-colors"
            >
              Sobre Nosotros
            </Link>
          </div>

          {/* Acciones derecha */}
          <div className="flex items-center space-x-4">
            <Link to="/productos" className="p-2 text-[#5F6670] hover:text-[#0A0A0A] transition-colors">
              <Search className="h-6 w-6" />
            </Link>

            {user ? (
              <div className="relative">
                <button
                  onClick={() => setIsMenuOpen(!isMenuOpen)}
                  className="flex items-center space-x-2 p-1 pl-3 rounded-full bg-[#F3F4F6] text-[#0A0A0A] hover:bg-[#EEF0F3] transition-colors border border-[#E5E7EB]"
                >
                  <span className="text-sm font-bold hidden sm:block">
                    {user.name || user.email.split('@')[0]}
                  </span>
                  <div className="p-1.5 rounded-full bg-white shadow-sm">
                    <User className="h-5 w-5" />
                  </div>
                  <ChevronDown className={`h-4 w-4 transition-transform ${isMenuOpen ? 'rotate-180' : ''}`} />
                </button>

                {isMenuOpen && (
                  <>
                    <div 
                      className="fixed inset-0 z-10" 
                      onClick={() => setIsMenuOpen(false)}
                    />
                    <div className="absolute right-0 mt-2 w-56 origin-top-right rounded-2xl bg-white shadow-2xl ring-1 ring-black ring-opacity-5 focus:outline-none z-20 overflow-hidden border border-[#E5E7EB] animate-in fade-in zoom-in duration-200">
                      <div className="py-1">
                        <div className="px-4 py-3 border-b border-[#F3F4F6]">
                          <p className="text-xs text-[#5F6670] font-medium">Conectado como</p>
                          <p className="text-sm font-bold text-[#0A0A0A] truncate">{user.email}</p>
                          <p className="text-[10px] uppercase tracking-wider text-[#9CA3AF] mt-1 font-black">
                            {user.role}
                          </p>
                        </div>
                        <Link
                          to={profilePath}
                          className="flex items-center px-4 py-3 text-sm font-semibold text-[#5F6670] hover:text-[#0A0A0A] hover:bg-[#F9FAFB] transition-colors"
                          onClick={() => setIsMenuOpen(false)}
                        >
                          <User className="mr-3 h-4 w-4" />
                          Mi Perfil
                        </Link>
                        <button
                          onClick={handleLogout}
                          className="flex w-full items-center px-4 py-3 text-sm font-semibold text-red-600 hover:bg-red-50 transition-colors"
                        >
                          <LogOut className="mr-3 h-4 w-4" />
                          Cerrar Sesión
                        </button>
                      </div>
                    </div>
                  </>
                )}
              </div>
            ) : (
              <div className="flex items-center space-x-2">
                <Link to="/login">
                  <Button variant="ghost" className="hidden lg:flex items-center gap-2">
                    <LogIn className="h-5 w-5" />
                    Iniciar Sesión
                  </Button>
                </Link>
                <Link to="/registro">
                  <Button className="flex items-center gap-2">
                    <UserPlus className="h-5 w-5" />
                    Registrarse
                  </Button>
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
