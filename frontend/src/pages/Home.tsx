import { Navbar } from "../components/Navbar";
import { Button } from "../components/ui/button";
import { Link } from "react-router-dom";
import { ArrowRight, ShoppingBag, Sparkles, ShieldCheck, User } from "lucide-react";


export default function Home() {
  return (
    <div className="min-h-screen bg-white">
      <Navbar />
      
      {/* Hero Section */}
      <section className="pt-32 pb-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="text-center space-y-8">
            <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-[#F3F4F6] text-[#0A0A0A] text-sm font-bold animate-fade-in">
              <Sparkles className="h-4 w-4 text-yellow-500" />
              La nueva era de la moda local
            </div>
            
            <h1 className="text-6xl md:text-8xl font-black tracking-tighter text-[#0A0A0A] leading-[0.9]">
              CONECTANDO <br />
              <span className="text-[#5F6670]">ESTILO Y MARCAS</span>
            </h1>
            
            <p className="max-w-2xl mx-auto text-xl text-[#5F6670] leading-relaxed">
              Descubre las mejores marcas de ropa locales en un solo lugar. 
              Fashtoll es el intermediario inteligente que estabas esperando.
            </p>
            
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4 pt-10">
              <Link to="/productos">
                <Button size="lg" className="group h-16 px-8 rounded-2xl text-lg">
                  Explorar Catálogo
                  <ArrowRight className="ml-2 h-5 w-5 transition-transform group-hover:translate-x-1" />
                </Button>
              </Link>
              <Link to="/registro">
                <Button size="lg" variant="outline" className="h-16 px-8 rounded-2xl text-lg">
                  Registrar mi Marca
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-[#F9FAFB]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-3 gap-12">
            <div className="space-y-4 p-8 bg-white rounded-[32px] border border-[#E5E7EB] shadow-sm">
              <div className="h-12 w-12 rounded-2xl bg-[#EEF0F3] flex items-center justify-center">
                <ShoppingBag className="h-6 w-6 text-[#0A0A0A]" />
              </div>
              <h3 className="text-xl font-bold text-[#0A0A0A]">Catálogo Curado</h3>
              <p className="text-[#5F6670]">Accede a una amplia selección de prendas con filtros avanzados.</p>
            </div>
            
            <div className="space-y-4 p-8 bg-white rounded-[32px] border border-[#E5E7EB] shadow-sm">
              <div className="h-12 w-12 rounded-2xl bg-[#EEF0F3] flex items-center justify-center">
                <ShieldCheck className="h-6 w-6 text-[#0A0A0A]" />
              </div>
              <h3 className="text-xl font-bold text-[#0A0A0A]">Marcas Verificadas</h3>
              <p className="text-[#5F6670]">Compre con confianza. Validamos cada marca en nuestra plataforma.</p>
            </div>

            <div className="space-y-4 p-8 bg-white rounded-[32px] border border-[#E5E7EB] shadow-sm">
              <div className="h-12 w-12 rounded-2xl bg-[#EEF0F3] flex items-center justify-center">
                <User className="h-6 w-6 text-[#0A0A0A]" />
              </div>
              <h3 className="text-xl font-bold text-[#0A0A0A]">Experiencia Personal</h3>
              <p className="text-[#5F6670]">Gestiona tu perfil y favoritos de forma sencilla.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
