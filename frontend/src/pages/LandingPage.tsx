import { Link } from "react-router";
import { Navbar } from "../components/Navbar";
import { Button } from "../components/ui/button";
import { Card, CardContent } from "../components/ui/card";
import { ImageWithFallback } from "../components/figma/ImageWithFallback";
import { Search, TrendingUp, Shield, Sparkles } from "lucide-react";
import logo from "figma:asset/0f36e0dfbf0178b7cf9b732204aaed075143dd20.png";

export default function LandingPage() {
  const features = [
    {
      icon: Search,
      title: "Búsqueda Inteligente",
      description: "Encuentra las prendas perfectas con nuestro motor de búsqueda avanzado",
    },
    {
      icon: TrendingUp,
      title: "Tendencias Actuales",
      description: "Descubre las últimas tendencias de moda de las mejores marcas colombianas",
    },
    {
      icon: Shield,
      title: "Compra Segura",
      description: "Conectamos directamente con marcas verificadas para tu seguridad",
    },
    {
      icon: Sparkles,
      title: "Personalización",
      description: "Recomendaciones personalizadas basadas en tus preferencias",
    },
  ];

  return (
    <div className="min-h-screen bg-[#F7F7F8]">
      <Navbar />

      <section className="pt-36 pb-24 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex flex-col items-center text-center">
            {/* Logo principal */}
            <div className="mb-6">
              <img 
                src={logo} 
                alt="Fashtoll - Scroll Less, Style More" 
                className="w-full max-w-md h-auto"
              />
            </div>

            <h1 className="text-5xl md:text-6xl font-bold leading-tight text-[#0A0A0A] mb-6">
              Descubre tu estilo con{" "}
              <span className="text-[#0A0A0A]">
                Fashtoll
              </span>
            </h1>

            <p className="text-xl text-[#5F6670] leading-relaxed max-w-3xl mb-8">
              <span className="block font-semibold text-[#0A0A0A] mb-2">
                Scroll Less, Style More
              </span>
              La plataforma que conecta a clientes con las mejores marcas de moda del país.
              Encuentra las prendas perfectas con nuestro motor de búsqueda inteligente.
            </p>

            <div className="flex flex-col sm:flex-row gap-4">
              <Link to="/registro">
                <Button
                  size="lg"
                  className="w-full sm:w-auto rounded-2xl bg-gradient-to-r from-[#111111] to-[#2B2B2B] text-white text-lg px-8 hover:opacity-90"
                >
                  Comenzar ahora
                </Button>
              </Link>

              <Link to="/buscar">
                <Button
                  size="lg"
                  variant="outline"
                  className="w-full sm:w-auto rounded-2xl border border-[#D1D5DB] bg-white text-[#0A0A0A] text-lg px-8 hover:bg-[#F3F4F6]"
                >
                  Explorar prendas
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      <section className="py-20 px-4 sm:px-6 lg:px-8 bg-white">
        <div className="max-w-7xl mx-auto">
          <div className="text-center mb-16">
            <h2 className="text-4xl font-bold text-[#0A0A0A] mb-4">
              ¿Por qué elegir Fashtoll?
            </h2>
            <p className="text-xl text-[#5F6670]">
              Todo lo que necesitas para una experiencia de compra única
            </p>
          </div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            {features.map((feature, index) => {
              const Icon = feature.icon;
              return (
                <Card
                  key={index}
                  className="rounded-[24px] border border-[#E5E7EB] bg-white shadow-sm transition-all hover:shadow-lg hover:border-[#B8BEC6]"
                >
                  <CardContent className="pt-6">
                    <div className="rounded-full w-14 h-14 bg-[#EEF0F3] flex items-center justify-center mb-4">
                      <Icon className="h-7 w-7 text-[#8F98A3]" />
                    </div>
                    <h3 className="text-xl font-semibold text-[#0A0A0A] mb-2">
                      {feature.title}
                    </h3>
                    <p className="text-[#5F6670]">{feature.description}</p>
                  </CardContent>
                </Card>
              );
            })}
          </div>
        </div>
      </section>

      <section className="py-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl mx-auto text-center">
          <div className="rounded-[32px] p-12 bg-gradient-to-r from-[#111111] to-[#2B2B2B] text-white shadow-xl">
            <h2 className="text-4xl font-bold mb-4">¿Listo para comenzar?</h2>
            <p className="text-xl mb-8 opacity-90">
              Únete para tener una experiencia de compra única, donde encontrarás lo que buscas
            </p>

            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/registro/cliente">
                <Button
                  size="lg"
                  variant="secondary"
                  className="w-full sm:w-auto text-lg px-8 rounded-2xl bg-white text-[#0A0A0A] hover:bg-[#F3F4F6]"
                >
                  Registrarse como Cliente
                </Button>
              </Link>

              <Link to="/registro/marca">
                <Button
                  size="lg"
                  variant="secondary"
                  className="w-full sm:w-auto text-lg px-8 rounded-2xl bg-[#D1D5DB] text-[#0A0A0A] hover:bg-[#E5E7EB]"
                >
                  Registrarse como Marca
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      <footer className="py-12 px-4 sm:px-6 lg:px-8 border-t border-[#E5E7EB]">
        <div className="max-w-7xl mx-auto text-center text-[#5F6670]">
          <p>&copy; 2026 Fashtoll. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  );
}