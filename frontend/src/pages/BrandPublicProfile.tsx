import { useParams, Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { getPublicBrandById } from "../services/brandPublicService";
import { useAuth } from "../hooks/useAuth"; // Ajusta la ruta según tu proyecto
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { followBrand, unfollowBrand, getFollowedBrands } from "../services/clientService";
import { 
  Star, 
  Users, 
  ExternalLink, 
  ArrowLeft, 
  Building2, 
  Loader2, 
  MessageSquare,
  AlertCircle
} from "lucide-react";
import { VerifiedBadge } from "../components/ui/VerifiedBadge";
import { Button } from "../components/ui/button";

// Reutilizamos el diseño decorativo de reseñas
function StarRating({ value, size = "sm" }: { value: number; size?: "sm" | "lg" }) {
  const cls = size === "lg" ? "h-6 w-6" : "h-4 w-4";
  return (
    <div className="flex items-center gap-0.5">
      {[1, 2, 3, 4, 5].map((i) => (
        <Star
          key={i}
          className={`${cls} transition-colors ${
            i <= Math.round(value)
              ? "fill-yellow-400 text-yellow-400"
              : "fill-gray-200 text-gray-200"
          }`}
        />
      ))}
    </div>
  );
}

const FAKE_REVIEWS = [
  {
    id: 1,
    author: "Fashion Enthusiast",
    avatar: "F",
    rating: 5,
    date: "hace 1 semana",
    text: "Mi marca favorita en Fashtoll. Siempre a la moda y con excelente calidad.",
  },
  {
    id: 2,
    author: "User_442",
    avatar: "U",
    rating: 5,
    date: "hace 3 semanas",
    text: "Los envíos son rápidos y las prendas son tal cual las fotos. 10/10.",
  },
  {
    id: 3,
    author: "Style Guide",
    avatar: "S",
    rating: 4,
    date: "hace 1 mes",
    text: "Diseños innovadores. Me gustaría que tuvieran más stock de las tallas grandes.",
  },
];

export default function BrandPublicProfile() {
  const { id } = useParams<{ id: string }>();

  const queryClient = useQueryClient();

  // 1. Extraemos el usuario global del contexto
  const { user } = useAuth();

  // 2. Extraemos el token que ya viaja dentro de la estructura de ese usuario
  const token = user?.token;

  // 3. Condición estricta: Debe haber token y el rol de usuario debe ser "CLIENT"
  const isClient = token && user?.role === "CLIENT";

  // 4. Consultamos la lista de marcas seguidas por este cliente usando el token
  const { data: followedBrands } = useQuery({
    queryKey: ["followedBrands"],
    queryFn: () => getFollowedBrands(token || ""),
                                            enabled: !!isClient, // Solo se ejecuta si el rol es CLIENT
  });

  // 5. Validamos si el cliente sigue a la marca de esta página en específico
  const isFollowing = followedBrands?.some((b: any) => b.id === Number(id)) || false;

  const followMut = useMutation({
    mutationFn: () => followBrand(Number(id), token || ""),
                                onSuccess: () => {
                                  queryClient.invalidateQueries({ queryKey: ["followedBrands"] });
                                  queryClient.invalidateQueries({ queryKey: ["publicBrand", id] });
                                }
  });

  const unfollowMut = useMutation({
    mutationFn: () => unfollowBrand(Number(id), token || ""),
                                  onSuccess: () => {
                                    queryClient.invalidateQueries({ queryKey: ["followedBrands"] });
                                    queryClient.invalidateQueries({ queryKey: ["publicBrand", id] });
                                  }
  });

  const handleFollowClick = () => {
    if (isFollowing) {
      unfollowMut.mutate();
    } else {
      followMut.mutate();
    }
  };

  const { data: brand, isLoading, isError } = useQuery({
    queryKey: ["publicBrand", id],
    queryFn: () => getPublicBrandById(Number(id)),
    enabled: !!id,
  });

  if (isLoading) {
    return (
      <div className="min-h-screen bg-[#FDFDFD]">
        <Navbar />
        <div className="flex flex-col items-center justify-center min-h-screen gap-6">
          <Loader2 className="h-12 w-12 animate-spin text-black" />
          <p className="text-xl font-bold text-[#0A0A0A] tracking-tight">Cargando perfil de marca...</p>
        </div>
      </div>
    );
  }

  if (isError || !brand) {
    return (
      <div className="min-h-screen bg-[#FDFDFD]">
        <Navbar />
        <div className="flex flex-col items-center justify-center min-h-screen gap-6 px-4">
          <div className="h-24 w-24 bg-red-50 rounded-full flex items-center justify-center">
            <Building2 className="h-10 w-10 text-red-400" />
          </div>
          <h2 className="text-2xl font-black text-[#0A0A0A]">Marca no encontrada</h2>
          <Link to="/marcas">
            <Button className="rounded-2xl h-12 px-8 font-bold">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Volver al catálogo
            </Button>
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#FDFDFD] selection:bg-black selection:text-white">
      <Navbar />

      <main className="pt-28 max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 pb-20">
        {/* Breadcrumb */}
        <Link 
          to="/marcas" 
          className="inline-flex items-center gap-2 text-sm font-semibold text-[#5F6670] hover:text-[#0A0A0A] transition-colors mb-12 group"
        >
          <ArrowLeft className="h-4 w-4 transition-transform group-hover:-translate-x-1" />
          Volver a marcas
        </Link>

        {/* Hero Section */}
        <div className="bg-white rounded-[48px] p-8 sm:p-12 border border-[#E5E7EB] shadow-[0_20px_50px_rgb(0,0,0,0.04)] mb-20 animate-in fade-in zoom-in duration-500">
          <div className="flex flex-col items-center text-center space-y-8">
            {/* Avatar Circle */}
            <div className="relative group">
              <div className="h-40 w-40 rounded-full overflow-hidden bg-[#F3F4F6] border-4 border-white shadow-xl transition-transform duration-700 group-hover:scale-105">
                {brand.pictureUrl ? (
                  <img 
                    src={brand.pictureUrl} 
                    alt={brand.name} 
                    className="h-full w-full object-cover"
                  />
                ) : (
                  <div className="h-full w-full flex items-center justify-center text-[#9CA3AF]">
                    <Building2 className="h-20 w-20 stroke-1" />
                  </div>
                )}
              </div>
              {brand.isVerified && (
                <VerifiedBadge size="lg" className="absolute bottom-2 right-2 border-4 border-white rounded-full bg-white shadow-lg" />
              )}
            </div>

            {/* Basic Info */}
            <div className="space-y-4">
              <div className="flex items-center justify-center gap-3">
                <h1 className="text-4xl sm:text-6xl font-black tracking-tight text-[#0A0A0A] capitalize">
                  {brand.name.toLowerCase()}
                </h1>
                {brand.isVerified && (
                  <VerifiedBadge size="lg" className="hidden sm:block" />
                )}
              </div>
              <p className="text-[#5F6670] font-medium max-w-md mx-auto">
                Marca oficial registrada en el ecosistema de Fashtoll. Líder en tendencias y calidad.
              </p>
            </div>

            {/* Stats Row */}
            <div className="flex flex-wrap items-center justify-center gap-8 py-8 border-y border-[#F3F4F6] w-full">
              <div className="text-center group">
                <p className="text-3xl font-black text-[#0A0A0A] group-hover:scale-110 transition-transform">
                  {brand.rating ? brand.rating.toFixed(1) : "0.0"}
                </p>
                <div className="flex items-center gap-1 justify-center mt-1">
                  <StarRating value={brand.rating} />
                </div>
                <p className="text-[10px] font-black uppercase tracking-[0.2em] text-[#9CA3AF] mt-2">Rating</p>
              </div>

              <div className="h-12 w-px bg-gray-100 hidden sm:block" />

              <div className="text-center group">
                <p className="text-3xl font-black text-[#0A0A0A] group-hover:scale-110 transition-transform">
                  {brand.followers?.toLocaleString() || 0}
                </p>
                <div className="flex items-center gap-1 justify-center mt-1 text-blue-500">
                  <Users className="h-4 w-4" />
                </div>
                <p className="text-[10px] font-black uppercase tracking-[0.2em] text-[#9CA3AF] mt-2">Seguidores</p>
              </div>
            </div>

            {/* Contenedor Horizontal Exclusivo para los Botones de Acción */}
            <div className="flex flex-row items-center justify-center gap-4 w-full">

            {/* Botón 1: Visitar tienda oficial (A la izquierda) */}
            {brand.linkOfficial && (
              <a
              href={brand.linkOfficial.startsWith('http') ? brand.linkOfficial : `https://${brand.linkOfficial}`}
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center justify-center gap-2 h-16 px-10 bg-[#0A0A0A] hover:bg-black text-white font-bold rounded-[20px] shadow-xl hover:shadow-2xl transition-all hover:scale-105 active:scale-95 group"
              >
              Visitar tienda oficial
              <ExternalLink className="h-5 w-5 transition-transform group-hover:translate-x-1 group-hover:-translate-y-1" />
              </a>
            )}

            {/* Botón 2: Seguir Marca (A la derecha de forma estricta) */}
            {isClient && (
              <Button
              onClick={handleFollowClick}
              disabled={followMut.isPending || unfollowMut.isPending}
              className={`h-16 px-10 font-bold rounded-[20px] shadow-xl transition-all hover:scale-105 active:scale-95 ${
                isFollowing
                ? "bg-gray-200 hover:bg-gray-300 text-black border border-gray-300"
                : "bg-white hover:bg-gray-50 text-black border-2 border-black"
              }`}
              >
              {followMut.isPending || unfollowMut.isPending ? (
                <Loader2 className="h-5 w-5 animate-spin" />
              ) : isFollowing ? (
                "Dejar de seguir"
              ) : (
                "Seguir"
              )}
              </Button>
            )}

            </div> {/* Fin del contenedor horizontal */}
            {/*
            {brand.linkOfficial && (
              <a 
                href={brand.linkOfficial.startsWith('http') ? brand.linkOfficial : `https://${brand.linkOfficial}`}
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center gap-2 h-16 px-10 bg-[#0A0A0A] hover:bg-black text-white font-bold rounded-[20px] shadow-xl hover:shadow-2xl transition-all hover:scale-105 active:scale-95 group"
              >
                Visitar tienda oficial
                <ExternalLink className="h-5 w-5 transition-transform group-hover:translate-x-1 group-hover:-translate-y-1" />
              </a>
            )}


            {isClient && (
              <Button
              onClick={handleFollowClick}
              disabled={followMut.isPending || unfollowMut.isPending}
              className={`h-16 px-10 font-bold rounded-[20px] shadow-xl transition-all hover:scale-105 active:scale-95 ${
                isFollowing
                ? "bg-gray-200 hover:bg-gray-300 text-black border border-gray-300"
                : "bg-white hover:bg-gray-50 text-black border-2 border-black"
              }`}
              >
              {followMut.isPending || unfollowMut.isPending ? (
                <Loader2 className="h-5 w-5 animate-spin" />
              ) : isFollowing ? (
                "Dejar de seguir"
              ) : (
                "Seguir"
              )}
              </Button>
            )}
            */}

          </div>
        </div>

        {/* Reviews Section (Decorativa) */}
        <div className="space-y-10 animate-in fade-in slide-in-from-bottom-8 duration-700">
          <div className="flex items-center gap-3">
            <div className="h-10 w-10 bg-black rounded-[14px] flex items-center justify-center text-white">
              <MessageSquare className="h-5 w-5" />
            </div>
            <h2 className="text-2xl font-black tracking-tight text-[#0A0A0A]">Reseñas de la marca</h2>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {FAKE_REVIEWS.map((review) => (
              <div key={review.id} className="p-8 bg-white rounded-[32px] border border-[#E5E7EB] space-y-4 shadow-sm hover:shadow-md transition-shadow">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-10 w-10 rounded-full bg-gray-100 flex items-center justify-center font-bold text-[#0A0A0A]">
                      {review.avatar}
                    </div>
                    <div>
                      <p className="text-sm font-bold text-[#0A0A0A]">{review.author}</p>
                      <p className="text-[10px] text-[#9CA3AF] font-medium">{review.date}</p>
                    </div>
                  </div>
                  <StarRating value={review.rating} />
                </div>
                <p className="text-sm text-[#5F6670] leading-relaxed font-medium">"{review.text}"</p>
              </div>
            ))}
          </div>

          <div className="inline-flex items-start gap-3 p-5 bg-blue-50 rounded-[24px] border border-blue-100">
            <AlertCircle className="h-5 w-5 text-blue-400 flex-shrink-0 mt-0.5" />
            <p className="text-xs font-semibold text-blue-700 leading-relaxed">
              Estamos trabajando para habilitar las reseñas públicas. Esta sección es una vista previa del diseño final del ecosistema Fashtoll.
            </p>
          </div>
        </div>
      </main>
    </div>
  );
}
