import { useState, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { ProductCard } from "../components/ui/ProductCard";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Slider } from "../components/ui/slider";
import { searchProducts, type SearchParams } from "../services/searchService";
import { getProductTypes, getTags } from "../services/productService";
import {
  Search,
  Filter,
  ChevronLeft,
  ChevronRight,
  Loader2,
  X,
  ChevronDown,
  ChevronUp,
  Maximize2,
  Minimize2,
  LayoutGrid,
  ShoppingBag,
  Sparkles,
  Star,
  Target
} from "lucide-react";

// Categorías del sistema
const Category = {
  TOPS: "TOPS",
  BOTTOMS: "BOTTOMS",
  OUTERWEAR: "OUTERWEAR",
  FULL_BODY: "FULL BODY"
} as const;

// Configuración de Colores
const COLOR_CONFIG: Record<string, { label: string, colorClass: string, style?: React.CSSProperties }> = {
  WHITE: { label: "Blanco", colorClass: "bg-white border-gray-200" },
  BLACK: { label: "Negro", colorClass: "bg-black border-black" },
  GREY: { label: "Gris", colorClass: "bg-gray-400 border-gray-400" },
  BROWN: { label: "Café", colorClass: "bg-[#5D4037] border-[#5D4037]" },
  BEIGE: { label: "Beige", colorClass: "bg-[#F5F5DC] border-[#E6E6CC]" },
  GREEN: { label: "Verde", colorClass: "bg-green-600 border-green-600" },
  BLUE: { label: "Azul", colorClass: "bg-blue-600 border-blue-600" },
  PURPLE: { label: "Morado", colorClass: "bg-purple-600 border-purple-600" },
  RED: { label: "Rojo", colorClass: "bg-red-600 border-red-600" },
  ORANGE: { label: "Naranja", colorClass: "bg-orange-500 border-orange-500" },
  PINK: { label: "Rosa", colorClass: "bg-pink-400 border-pink-400" },
  YELLOW: { label: "Amarillo", colorClass: "bg-yellow-400 border-yellow-400" },
  GOLD: { label: "Dorado", colorClass: "bg-[#D4AF37] border-[#D4AF37]" },
  SILVER: { label: "Plateado", colorClass: "bg-[#C0C0C0] border-[#C0C0C0]" },
  MULTICOLOR: {
    label: "Multicolor",
    colorClass: "border-transparent",
    style: { background: 'conic-gradient(from 0deg, #ff0000, #ff7f00, #ffff00, #00ff00, #0000ff, #4b0082, #8b00ff, #ff0000)' }
  },
  OTHER: { label: "Otro", colorClass: "bg-white border-dashed border-gray-300" }
};

export default function Catalog() {
  const [params, setParams] = useState<SearchParams>({
    query: "",
    page: 0,
    size: 12,
  });

  const [pendingParams, setPendingParams] = useState<SearchParams>({ ...params });
  const [keywordInput, setKeywordInput] = useState("");

  const [isFilterPanelOpen, setIsFilterPanelOpen] = useState(true);
  const [expandedCategories, setExpandedCategories] = useState<string[]>(Object.values(Category));
  const [expandedTagGroups, setExpandedTagGroups] = useState<string[]>(["Fit", "Estilo", "Ocasión"]);

  const { data: productTypes = [] } = useQuery({
    queryKey: ["productTypes"],
    queryFn: () => getProductTypes(""),
  });

  const { data: tags = [] } = useQuery({
    queryKey: ["tags"],
    queryFn: () => getTags(""),
  });

  const { data, isLoading, isError, isFetching } = useQuery({
    queryKey: ["productsSearch", params],
    queryFn: () => searchProducts(params),
    placeholderData: (prev) => prev,
  });

  const typesByCategory = useMemo(() => {
    const groups: Record<string, typeof productTypes> = {};
    Object.values(Category).forEach(cat => groups[cat] = []);
    productTypes.forEach(t => {
      if (groups[t.category]) groups[t.category].push(t);
    });
    return groups;
  }, [productTypes]);

  const tagsByType = useMemo(() => {
    const groups: Record<string, typeof tags> = {
      Fit: [],
      Estilo: [],
      Ocasión: []
    };

    const typeMapping: Record<string, string> = {
      "FIT": "Fit",
      "STYLE": "Estilo",
      "OCCASION": "Ocasión",
      "Fit": "Fit",
      "Style": "Estilo",
      "Occasion": "Ocasión"
    };

    tags.forEach(t => {
      const normalizedType = typeMapping[t.type] || t.type;
      if (groups[normalizedType]) {
        groups[normalizedType].push(t);
      }
    });

    return Object.fromEntries(Object.entries(groups).filter(([, v]) => v.length > 0));
  }, [tags]);

  const handleKeywordSearch = (e?: React.FormEvent) => {
    e?.preventDefault();
    setParams({ ...params, query: keywordInput, page: 0 });
  };

  const clearKeyword = () => {
    setKeywordInput("");
    setParams({ ...params, query: "", page: 0 });
  };

  const applyFilters = () => {
    setParams({ ...pendingParams, query: keywordInput, page: 0 });
  };

  const resetFilters = () => {
    const defaultParams = { query: keywordInput, page: 0, size: 12 };
    setPendingParams(defaultParams);
    setParams(defaultParams);
  };

  const togglePendingTag = (tagName: string) => {
    const currentTags = pendingParams.tags || [];
    if (currentTags.includes(tagName)) {
      setPendingParams({ ...pendingParams, tags: currentTags.filter(t => t !== tagName) });
    } else {
      setPendingParams({ ...pendingParams, tags: [...currentTags, tagName] });
    }
  };

  const toggleCategorySection = (cat: string) => {
    setExpandedCategories(prev => prev.includes(cat) ? prev.filter(c => c !== cat) : [...prev, cat]);
  };

  const toggleTagSection = (type: string) => {
    setExpandedTagGroups(prev => prev.includes(type) ? prev.filter(t => t !== type) : [...prev, type]);
  };

  const handlePageChange = (newPage: number) => {
    setParams({ ...params, page: newPage });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const renderPagination = () => {
    if (!data || data.totalPages <= 1) return null;
    const { currentPage, totalPages } = data;
    const pages = [];

    for (let i = 0; i < totalPages; i++) {
      pages.push(
        <button
          key={i}
          onClick={() => handlePageChange(i)}
          className={`pag-btn ${currentPage === i ? 'active' : 'inactive'}`}
        >
          {i + 1}
        </button>
      );
    }

    return (
      <div className="flex items-center justify-center gap-2 pt-12">
        <Button
          variant="ghost"
          disabled={currentPage === 0}
          onClick={() => handlePageChange(currentPage - 1)}
          className="rounded-2xl hover:bg-gray-100 font-bold text-xs tracking-wide px-4"
        >
          <ChevronLeft className="h-4 w-4 mr-1" /> Anterior
        </Button>
        <div className="flex items-center gap-1 mx-4">
          {pages}
        </div>
        <Button
          variant="ghost"
          disabled={currentPage === totalPages - 1}
          onClick={() => handlePageChange(currentPage + 1)}
          className="rounded-2xl hover:bg-gray-100 font-bold text-xs tracking-wide px-4"
        >
          Siguiente <ChevronRight className="h-4 w-4 ml-1" />
        </Button>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-[#FDFDFD] selection:bg-black selection:text-white">
      <Navbar />

      {/* Ampliamos el contenedor para usar más pantalla (max-w-[1920px]) y redujimos px */}
      <div className="pt-28 max-w-[1920px] mx-auto px-4 sm:px-6 xl:px-12 pb-20">

        {/* Header con Buscador Giga - Ligeramente más compacto */}
        <div className="flex flex-col md:flex-row items-center justify-between gap-8 mb-16">
          <div className="flex flex-col gap-2">
            <h1 className="text-5xl md:text-6xl font-black tracking-tighter leading-[0.9]">
              Encuentra lo que buscas <br /> <span className="text-[#9CA3AF]">Sin límites</span>
            </h1>
            <p className="text-sm font-medium text-[#9CA3AF] ml-1">
              Catálogo global de Fashtoll
            </p>
          </div>

          <form onSubmit={handleKeywordSearch} className="relative w-full md:max-w-xl group">
            <div className="absolute inset-0 bg-black/5 rounded-[32px] blur-2xl group-focus-within:bg-black/10 transition-all duration-500" />
            <div className="relative flex items-center bg-white border border-gray-100 rounded-[32px] shadow-sm hover:shadow-lg transition-all duration-500 h-20 px-6 group-focus-within:border-black/20 group-focus-within:scale-[1.01]">
              <Search className="h-6 w-6 text-[#9CA3AF] group-focus-within:text-black transition-colors" />
              <input
                type="text"
                placeholder="¿Qué estás buscando hoy?"
                className="flex-1 bg-transparent border-none focus:ring-0 px-5 text-lg font-bold placeholder:text-[#9CA3AF]/70 tracking-tight"
                value={keywordInput}
                onChange={(e) => setKeywordInput(e.target.value)}
              />
              {keywordInput && (
                <button
                  type="button"
                  onClick={clearKeyword}
                  className="p-2 hover:bg-gray-100 rounded-full mr-3 transition-colors"
                >
                  <X className="h-5 w-5" />
                </button>
              )}
              <Button
                type="submit"
                className="h-12 px-8 rounded-2xl bg-[#0A0A0A] hover:bg-black text-white font-bold text-sm shadow-md transition-all"
              >
                Buscar
              </Button>
            </div>
          </form>
        </div>

        {/* Separación reducida a gap-10 para acercar Sidebar al Grid */}
        <div className="flex flex-col lg:flex-row gap-8 xl:gap-10">

          {/* Panel de Filtros - Ancho ajustado a 300px */}
          <aside className={`w-full lg:w-[300px] shrink-0 space-y-10 transition-all duration-700 ${isFilterPanelOpen ? 'opacity-100' : 'opacity-40'}`}>
            <div className="flex items-center justify-between sticky top-32 z-10 bg-[#FDFDFD]/90 backdrop-blur-xl py-3 border-b border-gray-100 mb-4">
              <div className="flex items-center gap-3">
                <div className="h-10 w-10 bg-black rounded-[16px] flex items-center justify-center text-white shadow-md">
                  <Filter className="h-5 w-5" />
                </div>
                <h2 className="text-2xl font-black tracking-tight">Filtros</h2>
              </div>
              <Button
                variant="ghost"
                size="sm"
                onClick={() => setIsFilterPanelOpen(!isFilterPanelOpen)}
                className="rounded-xl hover:bg-gray-100"
              >
                {isFilterPanelOpen ? <Minimize2 className="h-4 w-4" /> : <Maximize2 className="h-4 w-4" />}
              </Button>
            </div>

            {isFilterPanelOpen && (
              <div className="space-y-8 animate-in fade-in slide-in-from-left-4 duration-500">

                {/* Tipo de Producto & Categoría */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <LayoutGrid className="h-4 w-4" />
                    <h3 className="font-bold text-sm">Tipo & Categoría</h3>
                  </div>
                  <div className="space-y-2">
                    {Object.entries(typesByCategory).map(([cat, types]) => (
                      <div key={cat} className="space-y-1">
                        <button
                          onClick={() => toggleCategorySection(cat)}
                          className="flex items-center justify-between w-full py-2 group px-3 hover:bg-gray-50 rounded-xl transition-colors"
                        >
                          <span className={`text-sm font-semibold transition-colors capitalize ${pendingParams.category === cat ? 'text-black' : 'text-[#5F6670] group-hover:text-black'}`}>
                            {cat.toLowerCase()}
                          </span>
                          {expandedCategories.includes(cat) ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
                        </button>
                        {expandedCategories.includes(cat) && (
                          <div className="grid grid-cols-1 gap-1 py-2 px-1 animate-in slide-in-from-top-2">
                            {types.map(type => (
                              <button
                                key={type.id}
                                onClick={() => setPendingParams({ ...pendingParams, productType: pendingParams.productType === type.name ? undefined : type.name, category: cat })}
                                className={`text-left px-4 py-3 rounded-[14px] text-xs font-medium capitalize transition-all ${pendingParams.productType === type.name
                                    ? "bg-[#0A0A0A] text-white shadow-md scale-[1.01]"
                                    : "text-[#5F6670] hover:bg-gray-100 hover:text-black"
                                  }`}
                              >
                                {type.name.toLowerCase()}
                              </button>
                            ))}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                </div>

                {/* Fit General */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <Maximize2 className="h-4 w-4" />
                    <h3 className="font-bold text-sm">Ajuste / Fit</h3>
                  </div>
                  <div className="flex gap-2">
                    {["SLIM", "REGULAR", "LOOSE"].map(fit => (
                      <button
                        key={fit}
                        onClick={() => setPendingParams({ ...pendingParams, generalFit: pendingParams.generalFit === fit ? undefined : fit })}
                        className={`flex-1 h-12 rounded-[16px] text-xs font-semibold capitalize transition-all border ${pendingParams.generalFit === fit
                            ? "bg-black border-black text-white shadow-md scale-105"
                            : "bg-white border-gray-200 text-[#5F6670] hover:border-gray-300 hover:text-black"
                          }`}
                      >
                        {fit.toLowerCase()}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Género */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <Sparkles className="h-4 w-4" />
                    <h3 className="font-bold text-sm">Género</h3>
                  </div>
                  <div className="grid grid-cols-3 gap-2">
                    {["MALE", "FEMALE", "UNISEX"].map(g => (
                      <button
                        key={g}
                        onClick={() => setPendingParams({ ...pendingParams, gender: pendingParams.gender === g ? undefined : g })}
                        className={`h-12 rounded-[16px] text-xs font-semibold capitalize transition-all border ${pendingParams.gender === g
                            ? "bg-black border-black text-white shadow-md"
                            : "bg-white border-gray-200 text-[#5F6670] hover:border-gray-300 hover:text-black"
                          }`}
                      >
                        {g === "MALE" ? "Hombre" : g === "FEMALE" ? "Mujer" : "Unisex"}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Color */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <div className="h-3 w-3 rounded-full bg-gradient-to-tr from-pink-500 via-purple-500 to-indigo-500" />
                    <h3 className="font-bold text-sm">Color</h3>
                  </div>
                  <div className="flex flex-wrap gap-2.5 px-1">
                    {Object.entries(COLOR_CONFIG).map(([key, config]) => (
                      <button
                        key={key}
                        title={config.label}
                        onClick={() => setPendingParams({ ...pendingParams, color: pendingParams.color === key ? undefined : key })}
                        className={`relative h-10 w-10 rounded-full border transition-all p-0.5 hover:scale-110 ${pendingParams.color === key ? 'border-black scale-110 shadow-md' : 'border-transparent'
                          }`}
                      >
                        <div
                          className={`h-full w-full rounded-full border shadow-inner ${config.colorClass} flex items-center justify-center overflow-hidden`}
                          style={config.style}
                        >
                          {key === "OTHER" && <span className="text-[#9CA3AF] text-lg font-black leading-none mt-[-6px]">...</span>}
                        </div>
                        {pendingParams.color === key && (
                          <div className="absolute -top-1 -right-1 h-4 w-4 bg-black rounded-full flex items-center justify-center border-2 border-white shadow-sm">
                            <div className="h-1.5 w-1.5 bg-white rounded-full" />
                          </div>
                        )}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Precio */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <span className="font-bold text-sm">$</span>
                    <h3 className="font-bold text-sm">Precio</h3>
                  </div>
                  <div className="space-y-6 px-4 pt-2">
                    <Slider
                      value={[pendingParams.minPrice || 0, pendingParams.maxPrice || 4000000]}
                      max={4000000}
                      step={50000}
                      minStepsBetweenThumbs={1}
                      onValueChange={(vals) => setPendingParams({...pendingParams, minPrice: vals[0], maxPrice: vals[1]})}
                    />
                    <div className="flex flex-col items-center gap-4 pt-2">
                      <div className="w-full text-center py-2 bg-white border border-gray-100 rounded-2xl">
                        <span className="text-sm font-semibold">
                          ${(pendingParams.minPrice || 0).toLocaleString()} — ${(pendingParams.maxPrice || 4000000).toLocaleString()}
                        </span>
                      </div>
                      <div className="grid grid-cols-2 gap-3 w-full">
                        <Input
                          type="number"
                          placeholder="Mínimo"
                          className="h-12 rounded-xl border focus:border-black font-medium transition-all"
                          value={pendingParams.minPrice || ""}
                          onChange={(e) => setPendingParams({ ...pendingParams, minPrice: Number(e.target.value) || undefined })}
                        />
                        <Input
                          type="number"
                          placeholder="Máximo"
                          className="h-12 rounded-xl border focus:border-black font-medium transition-all"
                          value={pendingParams.maxPrice || ""}
                          onChange={(e) => setPendingParams({ ...pendingParams, maxPrice: Number(e.target.value) || undefined })}
                        />
                      </div>
                    </div>
                  </div>
                </div>

                {/* Tags & Estilo */}
                <div className="space-y-4">
                  <div className="flex items-center gap-2 text-black">
                    <Target className="h-4 w-4" />
                    <h3 className="font-bold text-sm">Tags & Estilo</h3>
                  </div>
                  <div className="space-y-3">
                    {Object.entries(tagsByType).map(([type, groupTags]) => (
                      <div key={type} className="space-y-2 bg-gray-50 p-4 rounded-2xl border border-gray-100/50">
                        <button
                          onClick={() => toggleTagSection(type)}
                          className="flex items-center justify-between w-full text-sm font-semibold text-black mb-1 capitalize"
                        >
                          {type.toLowerCase()}
                          {expandedTagGroups.includes(type) ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
                        </button>
                        {expandedTagGroups.includes(type) && (
                          <div className="flex flex-wrap gap-1.5 animate-in slide-in-from-top-2">
                            {groupTags.map(tag => {
                              const isSelected = pendingParams.tags?.includes(tag.name);
                              return (
                                <button
                                  key={tag.id}
                                  onClick={() => togglePendingTag(tag.name)}
                                  className={`px-3 py-1.5 rounded-full text-[11px] font-medium border transition-all flex items-center gap-1 capitalize ${isSelected
                                      ? "bg-[#0A0A0A] border-black text-white shadow-md scale-105"
                                      : "bg-white border-gray-200 text-[#5F6670] hover:border-gray-300 hover:text-black"
                                    }`}
                                >
                                  {tag.name.toLowerCase()}
                                  {isSelected && <X className="h-3 w-3 opacity-80" />}
                                </button>
                              );
                            })}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                </div>

                {/* Botones Finales */}
                <div className="sticky bottom-6 z-20 flex flex-col gap-3 pt-6 bg-[#FDFDFD]/90 backdrop-blur-xl">
                  <Button
                    onClick={applyFilters}
                    disabled={isFetching}
                    className="w-full h-14 rounded-2xl bg-[#0A0A0A] hover:bg-black font-bold text-sm shadow-lg hover:scale-[1.01] transition-all group"
                  >
                    {isFetching ? <Loader2 className="h-5 w-5 animate-spin" /> : (
                      <span className="flex items-center gap-2">
                        Aplicar filtros
                      </span>
                    )}
                  </Button>
                  <button
                    onClick={resetFilters}
                    className="text-xs font-semibold text-[#9CA3AF] hover:text-red-500 transition-colors py-2 flex items-center justify-center gap-1.5"
                  >
                    <X className="h-3 w-3" /> Restablecer filtros
                  </button>
                </div>
              </div>
            )}
          </aside>

          {/* Contenido de Productos - Ahora más ancho */}
          <main className="flex-1 space-y-10 min-w-0">
            <div className="flex flex-col sm:flex-row items-end justify-between gap-4 border-b border-gray-100 pb-6">
              <div className="space-y-1 text-center sm:text-left">
                <h3 className="text-2xl font-black tracking-tight">
                  Viendo {data?.searchedProducts.length || 0} de {data?.totalResults || 0} productos
                </h3>
              </div>

              <div className="flex gap-2">
                <Button variant="ghost" size="sm" className="h-10 rounded-xl font-semibold text-xs bg-white shadow-sm border border-gray-200 px-4">
                  <Star className="h-3.5 w-3.5 mr-2 fill-yellow-400 text-yellow-400" /> Populares
                </Button>
                <div className="h-10 w-10 rounded-xl bg-[#0A0A0A] text-white flex items-center justify-center shadow-md">
                  <LayoutGrid className="h-4 w-4" />
                </div>
              </div>
            </div>

            {/* Grid y Estados de Carga */}
            <div className="min-h-[800px] relative">
              {isLoading ? (
                <div className="absolute inset-0 flex flex-col items-center justify-center gap-6 animate-in fade-in duration-700">
                  <div className="relative">
                    <div className="h-20 w-20 rounded-full border-[6px] border-gray-100 border-t-black animate-[spin_0.8s_linear_infinite]" />
                    <div className="absolute inset-0 flex items-center justify-center">
                      <ShoppingBag className="h-6 w-6 animate-bounce text-black" />
                    </div>
                  </div>
                  <div className="text-center space-y-2">
                    <p className="text-2xl font-bold tracking-tight">Cargando...</p>
                  </div>
                </div>
              ) : isError ? (
                <div className="text-center py-20 space-y-6 bg-red-50/50 rounded-[40px] border border-red-100">
                  <div className="p-4 bg-red-100 rounded-full w-fit mx-auto">
                    <X className="h-8 w-8 text-red-500" />
                  </div>
                  <div className="space-y-2">
                    <h3 className="text-xl font-bold text-red-600">Ocurrió un error</h3>
                    <p className="text-sm font-medium text-red-400 max-w-sm mx-auto">
                      Tuvimos problemas para cargar el catálogo. Por favor reintenta la conexión.
                    </p>
                  </div>
                  <Button onClick={() => setParams({ ...params })} className="h-12 px-8 bg-red-600 hover:bg-red-700 rounded-xl font-bold text-xs shadow-md">Reintentar</Button>
                </div>
              ) : data?.searchedProducts.length === 0 ? (
                <div className="text-center py-32 space-y-8 bg-[#F9FAFB] rounded-[48px] border-2 border-dashed border-gray-200">
                  <div className="relative w-fit mx-auto">
                    <div className="h-24 w-24 bg-white rounded-[24px] flex items-center justify-center shadow-lg">
                      <Search className="h-10 w-10 text-[#E5E7EB]" />
                    </div>
                  </div>
                  <div className="space-y-3">
                    <h3 className="text-3xl font-bold tracking-tight">Sin resultados</h3>
                    <p className="text-[#9CA3AF] font-medium text-base">
                      No hay productos que coincidan con estos filtros.
                    </p>
                  </div>
                  <Button
                    onClick={resetFilters}
                    className="h-12 rounded-xl bg-[#0A0A0A] hover:bg-black font-bold text-sm px-10 shadow-md transition-all"
                  >
                    Restablecer filtros
                  </Button>
                </div>
              ) : (
                <div className={`grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4 gap-x-8 gap-y-12 transition-all duration-500 ${isFetching ? 'opacity-40 grayscale blur-[2px]' : 'opacity-100'}`}>
                  {data?.searchedProducts.map((product) => (
                    <ProductCard key={product.id} product={product} />
                  ))}
                </div>
              )}
            </div>

            {/* Paginación */}
            {renderPagination()}
          </main>
        </div>
      </div>

      <style>{`
        .pag-btn {
          @apply h-10 w-10 rounded-[14px] flex items-center justify-center text-sm font-bold transition-all hover:bg-gray-100 hover:scale-105 active:scale-95;
        }
        .pag-btn.active {
          @apply bg-[#0A0A0A] text-white shadow-md scale-110 z-10 opacity-100;
        }
        .pag-btn.inactive {
          @apply text-[#9CA3AF] opacity-60 bg-transparent hover:opacity-100 hover:text-black;
        }
      `}</style>
    </div>
  );
}
