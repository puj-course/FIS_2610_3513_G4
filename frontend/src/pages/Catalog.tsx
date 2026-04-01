import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { ProductCard } from "../components/ui/ProductCard";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { searchProducts } from "../services/searchService";
import { Search, Filter, ChevronLeft, ChevronRight, Loader2, X, ShoppingBag } from "lucide-react";



export default function Catalog() {
  const [params, setParams] = useState({
    keyword: "",
    category: "",
    gender: "",
    page: 0,
    size: 12,
  });

  const { data, isLoading, isError } = useQuery({
    queryKey: ["products", params],
    queryFn: () => searchProducts(params),
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setParams({ ...params, page: 0 });
  };

  return (
    <div className="min-h-screen bg-white">
      <Navbar />

      <div className="pt-24 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex flex-col md:flex-row gap-8 py-8">
          
          {/* Sidebar de Filtros */}
          <aside className="w-full md:w-64 space-y-8">
            <div className="flex items-center justify-between">
              <h2 className="text-xl font-black tracking-tighter uppercase">Filtros</h2>
              <Filter className="h-5 w-5" />
            </div>

            <div className="space-y-6">
              {/* Género */}
              <div className="space-y-3">
                <h3 className="font-bold text-sm uppercase tracking-wider text-[#5F6670]">Género</h3>
                <div className="flex flex-wrap gap-2">
                  {["M", "F", "U"].map((g) => (
                    <button
                      key={g}
                      onClick={() => setParams({ ...params, gender: params.gender === g ? "" : g, page: 0 })}
                      className={`px-4 py-2 rounded-xl text-xs font-bold transition-all ${
                        params.gender === g ? "bg-[#0A0A0A] text-white" : "bg-[#F3F4F6] text-[#5F6670] hover:bg-[#EEF0F3]"
                      }`}
                    >
                      {g === "M" ? "Hombre" : g === "F" ? "Mujer" : "Unisex"}
                    </button>
                  ))}
                </div>
              </div>

              {/* Categoría (Simulada) */}
              <div className="space-y-3">
                <h3 className="font-bold text-sm uppercase tracking-wider text-[#5F6670]">Categoría</h3>
                <div className="flex flex-col gap-1">
                  {["Camisetas", "Pantalones", "Chaquetas", "Calzado"].map((c) => (
                    <button
                      key={c}
                      onClick={() => setParams({ ...params, category: params.category === c ? "" : c, page: 0 })}
                      className={`text-left px-3 py-2 rounded-xl text-sm font-semibold transition-all ${
                        params.category === c ? "bg-[#F3F4F6] text-[#0A0A0A]" : "text-[#5F6670] hover:text-[#0A0A0A]"
                      }`}
                    >
                      {c}
                    </button>
                  ))}
                </div>
              </div>
            </div>
            
            {/* Limpiar Filtros */}
            {(params.category || params.gender || params.keyword) && (
              <Button 
                variant="ghost" 
                size="sm" 
                onClick={() => setParams({ keyword: "", category: "", gender: "", page: 0, size: 12 })}
                className="w-full text-xs font-bold underline"
              >
                Limpiar todo
              </Button>
            )}
          </aside>

          {/* Contenido Principal */}
          <main className="flex-1 space-y-8">
            {/* Barra de Búsqueda */}
            <form onSubmit={handleSearch} className="relative group">
              <Search className="absolute left-4 top-4 h-6 w-6 text-[#9CA3AF] group-focus-within:text-[#0A0A0A] transition-colors" />
              <Input
                placeholder="Buscar productos por nombre, marca o descripción..."
                className="h-14 pl-14 pr-14 rounded-2xl shadow-sm border-[#E5E7EB] bg-white transition-all focus:shadow-xl"
                value={params.keyword}
                onChange={(e) => setParams({ ...params, keyword: e.target.value })}
              />
              {params.keyword && (
                  <button 
                  type="button"
                  onClick={() => setParams({...params, keyword: "", page: 0})}
                  className="absolute right-4 top-4 p-1 hover:bg-[#F3F4F6] rounded-full"
                  >
                      <X className="h-4 w-4" />
                  </button>
              )}
            </form>

            <div className="flex items-center justify-between">
              <p className="text-sm text-[#5F6670] font-semibold">
                Mostrando <span className="text-[#0A0A0A]">{data?.content.length || 0}</span> de <span className="text-[#0A0A0A]">{data?.totalElements || 0}</span> productos
              </p>
            </div>

            {/* Grid de Productos */}
            {isLoading ? (
              <div className="flex flex-col items-center justify-center py-20 gap-4">
                <Loader2 className="h-10 w-10 animate-spin text-[#0A0A0A]" />
                <p className="text-[#5F6670] font-bold text-sm tracking-widest uppercase">Cargando catálogo...</p>
              </div>
            ) : isError ? (
              <div className="text-center py-20 space-y-4">
                <div className="text-red-500 font-bold">Error al cargar productos</div>
                <Button onClick={() => setParams({ ...params })}>Reintentar</Button>
              </div>
            ) : data?.content.length === 0 ? (
                <div className="text-center py-20 space-y-4 bg-[#F9FAFB] rounded-[40px]">
                    <ShoppingBag className="mx-auto h-12 w-12 text-[#9CA3AF]" />
                    <h3 className="text-xl font-bold">No encontramos resultados</h3>
                    <p className="text-[#5F6670]">Intenta ajustando los filtros o cambiando el término de búsqueda.</p>
                </div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
                    {data?.content.map((product) => (
                    <ProductCard key={product.id} product={product} />
                    ))}
                </div>
            )}

            {/* Paginación */}
            {data && data.totalPages > 1 && (
              <div className="flex items-center justify-center gap-4 pt-10">
                <Button
                  variant="outline"
                  size="sm"
                  disabled={params.page === 0}
                  onClick={() => setParams({ ...params, page: params.page - 1 })}
                  className="rounded-xl border-[#E5E7EB] hover:bg-[#F3F4F6]"
                >
                  <ChevronLeft className="h-5 w-5" />
                  Anterior
                </Button>
                <div className="text-sm font-bold bg-[#F3F4F6] px-4 py-2 rounded-xl">
                  {params.page + 1} / {data.totalPages}
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={params.page >= data.totalPages - 1}
                  onClick={() => setParams({ ...params, page: params.page + 1 })}
                  className="rounded-xl border-[#E5E7EB] hover:bg-[#F3F4F6]"
                >
                  Siguiente
                  <ChevronRight className="h-5 w-5" />
                </Button>
              </div>
            )}
          </main>
        </div>
      </div>
    </div>
  );
}
