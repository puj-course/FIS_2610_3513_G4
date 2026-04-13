import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { BrandCard } from "../components/ui/BrandCard";
import { getAllPublicBrands } from "../services/brandPublicService";
import { Search, Building2, Loader2, Sparkles } from "lucide-react";

export default function BrandListing() {
  const [searchTerm, setSearchTerm] = useState("");

  const { data: brands, isLoading, isError } = useQuery({
    queryKey: ["publicBrands"],
    queryFn: getAllPublicBrands,
  });

  const filteredBrands = brands?.filter(brand =>
    brand.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-[#FDFDFD] selection:bg-black selection:text-white">
      <Navbar />

      <main className="pt-32 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-20">

        {/* Header Section */}
        <div className="flex flex-col items-center text-center space-y-6 mb-16 animate-in fade-in slide-in-from-top-6 duration-700">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-blue-50 border border-blue-100 text-blue-600 text-xs font-black uppercase tracking-widest">
            <Sparkles className="h-4 w-4" />
            Marcas Oficiales
          </div>
          <h1 className="text-5xl sm:text-7xl font-black tracking-tight text-[#0A0A0A] max-w-2xl leading-[0.95]">
            Nuestras Marcas aliadas
          </h1>
          <p className="text-[#5F6670] text-lg font-medium max-w-xl">
            Descubre las mentes creativas detrás de las colecciones más exclusivas de Fashtoll.
          </p>

          {/* Search Bar (Decorativa + filtro local) */}
          <div className="relative w-full max-w-xl group pt-4">
            <div className="absolute inset-y-0 left-6 flex items-center pointer-events-none group-focus-within:text-black text-[#9CA3AF] transition-colors">
              <Search className="h-6 w-6 stroke-[2.5]" />
            </div>
            <input
              type="text"
              placeholder="Buscar marca por nombre..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full h-16 pl-16 pr-8 rounded-[24px] bg-white border border-[#E5E7EB] shadow-[0_8px_30px_rgb(0,0,0,0.04)] focus:shadow-[0_20px_50px_rgb(0,0,0,0.1)] focus:border-black transition-all outline-none text-lg font-semibold placeholder:text-[#9CA3AF]"
            />
          </div>
        </div>

        {/* Loading State */}
        {isLoading && (
          <div className="flex flex-col items-center justify-center py-20 gap-4 text-[#5F6670]">
            <Loader2 className="h-10 w-10 animate-spin" />
            <p className="font-bold">Cargando marcas...</p>
          </div>
        )}

        {/* Error State */}
        {isError && (
          <div className="p-8 bg-red-50 rounded-[32px] border border-red-100 text-center space-y-4">
            <Building2 className="h-12 w-12 text-red-400 mx-auto" />
            <h3 className="text-xl font-black text-red-900">Hubo un problema al cargar las marcas</h3>
            <p className="text-red-600 font-medium">Por favor, intenta de nuevo más tarde.</p>
          </div>
        )}

        {/* Grid Section */}
        {!isLoading && !isError && (
          <>
            {filteredBrands && filteredBrands.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {filteredBrands.map((brand) => (
                  <BrandCard key={brand.id} brand={brand} />
                ))}
              </div>
            ) : (
              <div className="text-center py-20 bg-gray-50 rounded-[40px] border-2 border-dashed border-gray-200">
                <Building2 className="h-16 w-16 text-gray-300 mx-auto mb-4 stroke-1" />
                <h3 className="text-2xl font-black text-gray-400 tracking-tight">
                  No se encontraron marcas
                </h3>
                <p className="text-gray-400 font-medium mt-2">
                  Prueba con términos como "Nike" o "Adidas"
                </p>
              </div>
            )}
          </>
        )}
      </main>
    </div>
  );
}
