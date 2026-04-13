import { useState, useMemo } from "react";
import { useParams, useLocation, Link, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { Navbar } from "../components/Navbar";
import { getProductById } from "../services/productDetailService";
import type { TagDetail } from "../services/productDetailService";
import { type Product } from "../services/searchService";
import { getPublicBrandById } from "../services/brandPublicService";
import {
  ChevronLeft,
  ChevronRight,
  Star,
  ExternalLink,
  ImageIcon,
  ArrowLeft,
  ShoppingBag,
  Tag,
  Ruler,
  Users,
  Palette,
  CalendarDays,
  MessageSquare,
  AlertCircle,
  Heart,
  Package,
  Building2,
} from "lucide-react";
import { VerifiedBadge } from "../components/ui/VerifiedBadge";
import { Button } from "../components/ui/button";

// ──────────────────────────────────────────────
// Helpers de traducción
// ──────────────────────────────────────────────
const TRANSLATIONS: Record<string, string> = {
  SLIM: "Slim",
  REGULAR: "Regular",
  LOOSE: "Holgado",
  MALE: "Hombre",
  FEMALE: "Mujer",
  UNISEX: "Unisex",
  WHITE: "Blanco",
  BLACK: "Negro",
  GREY: "Gris",
  BROWN: "Café",
  BEIGE: "Beige",
  GREEN: "Verde",
  BLUE: "Azul",
  PURPLE: "Morado",
  RED: "Rojo",
  ORANGE: "Naranja",
  PINK: "Rosado",
  YELLOW: "Amarillo",
  GOLD: "Dorado",
  SILVER: "Plateado",
  MULTICOLOR: "Multicolor",
  OTHER: "Otro",
  STYLE: "Estilo",
  FIT: "Fit",
  OCCASION: "Ocasión",
  TOPS: "Tops",
  BOTTOMS: "Bottoms",
  OUTERWEAR: "Outerwear",
  FULL_BODY: "Full Body",
};
const t = (v: string) => TRANSLATIONS[v] ?? v;

// Color swatch helper
const COLOR_STYLE: Record<string, { bg: string; style?: React.CSSProperties }> = {
  WHITE: { bg: "bg-white border border-gray-300" },
  BLACK: { bg: "bg-black" },
  GREY: { bg: "bg-gray-400" },
  BROWN: { bg: "bg-[#5D4037]" },
  BEIGE: { bg: "bg-[#F5F5DC] border border-gray-200" },
  GREEN: { bg: "bg-green-600" },
  BLUE: { bg: "bg-blue-600" },
  PURPLE: { bg: "bg-purple-600" },
  RED: { bg: "bg-red-600" },
  ORANGE: { bg: "bg-orange-500" },
  PINK: { bg: "bg-pink-400" },
  YELLOW: { bg: "bg-yellow-400" },
  GOLD: { bg: "bg-[#D4AF37]" },
  SILVER: { bg: "bg-[#C0C0C0]" },
  MULTICOLOR: {
    bg: "border border-transparent",
    style: {
      background:
        "conic-gradient(from 0deg, #ff0000, #ff7f00, #ffff00, #00ff00, #0000ff, #8b00ff, #ff0000)",
    },
  },
  OTHER: { bg: "bg-white border-dashed border-2 border-gray-300" },
};

// Tag type styles
const TAG_TYPE_STYLES: Record<
  string,
  { pill: string; group: string; label: string }
> = {
  FIT: {
    label: "Fit",
    group: "bg-orange-50 border border-orange-100",
    pill: "bg-orange-100 text-orange-700 border border-orange-200",
  },
  STYLE: {
    label: "Estilo",
    group: "bg-purple-50 border border-purple-100",
    pill: "bg-purple-100 text-purple-700 border border-purple-200",
  },
  OCCASION: {
    label: "Ocasión",
    group: "bg-blue-50 border border-blue-100",
    pill: "bg-blue-100 text-blue-700 border border-blue-200",
  },
};
const defaultTagStyle = {
  label: "Otros",
  group: "bg-gray-50 border border-gray-100",
  pill: "bg-gray-100 text-gray-700 border border-gray-200",
};

// Reviews decorativas no funcionales
const FAKE_REVIEWS = [
  {
    id: 1,
    author: "María G.",
    avatar: "M",
    rating: 5,
    date: "hace 2 semanas",
    text: "Excelente calidad, la tela es muy suave y el diseño es tal cual lo muestra la foto. Lo recomiendo.",
  },
  {
    id: 2,
    author: "Carlos R.",
    avatar: "C",
    rating: 4,
    date: "hace 1 mes",
    text: "Muy buena compra. El envío fue rápido y el producto llegó en perfectas condiciones.",
  },
  {
    id: 3,
    author: "Laura M.",
    avatar: "L",
    rating: 5,
    date: "hace 3 semanas",
    text: "Me encantó. La talla es tal cual la guía. Sin duda volveré a comprar.",
  },
];

function StarRating({
  value,
  size = "sm",
}: {
  value: number;
  size?: "sm" | "lg";
}) {
  const cls = size === "lg" ? "h-6 w-6" : "h-4 w-4";
  return (
    <div className="flex items-center gap-0.5">
      {[1, 2, 3, 4, 5].map((i) => (
        <Star
          key={i}
          className={`${cls} transition-colors ${i <= Math.round(value)
            ? "fill-yellow-400 text-yellow-400"
            : "fill-gray-200 text-gray-200"
            }`}
        />
      ))}
    </div>
  );
}

export default function ProductDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();
  const stateProduct = location.state?.product as Product | undefined;

  const [currentImage, setCurrentImage] = useState(0);

  const { data: detail, isLoading: isDetailLoading } = useQuery({
    queryKey: ["productDetail", id],
    queryFn: () => getProductById(Number(id)),
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
  });


  const isLoading = !stateProduct && isDetailLoading;

  const images: string[] = useMemo(() => {
    if (stateProduct?.imageUrls?.length) return stateProduct.imageUrls;
    if (detail?.imageUrls?.length) return detail.imageUrls;
    return [];
  }, [stateProduct, detail]);

  const typedTags: TagDetail[] = useMemo(() => {
    if (detail?.tags?.length) return detail.tags;
    if (stateProduct?.tags?.length) {
      return stateProduct.tags.map((name, i) => ({
        id: i,
        name,
        type: "STYLE",
      }));
    }
    return [];
  }, [detail, stateProduct]);

  const tagsByType = useMemo(() => {
    const groups: Record<string, TagDetail[]> = {};
    typedTags.forEach((tag) => {
      if (!groups[tag.type]) groups[tag.type] = [];
      groups[tag.type].push(tag);
    });
    return groups;
  }, [typedTags]);

  const name =
    stateProduct?.name ?? detail?.name ?? "";
  const description =
    stateProduct?.description ?? detail?.description ?? "";
  const price =
    stateProduct?.price ?? detail?.price ?? 0;
  const rating =
    stateProduct?.rating ?? detail?.rating ?? 0;
  const available =
    stateProduct?.available ?? detail?.available ?? true;
  const generalFit =
    stateProduct?.generalFit ?? detail?.generalFit ?? "";
  const gender =
    stateProduct?.gender ?? detail?.gender ?? "";
  const color =
    stateProduct?.color ?? detail?.color ?? "";
  const linkProduct =
    stateProduct?.linkProduct ?? detail?.linkProduct ?? "";
  const createdAt =
    stateProduct?.createdAt ?? detail?.createdAt ?? "";
  const category =
    stateProduct?.category ?? detail?.productType?.category ?? "";
  const productTypeName =
    stateProduct?.productTypeName ?? detail?.productType?.name ?? "";

  const brandId = stateProduct?.brandId ?? detail?.brandId;

  // Fetch brand detail if not provided in state
  const { data: brandDetail } = useQuery({
    queryKey: ["brandPublic", brandId],
    queryFn: () => getPublicBrandById(brandId!),
    enabled: !!brandId && !stateProduct,
  });

  const brandName = stateProduct?.brandName ?? brandDetail?.name ?? "";
  const brandPictureUrl = stateProduct?.brandPictureUrl ?? brandDetail?.pictureUrl ?? "";
  const brandIsVerified = stateProduct?.brandIsVerified ?? brandDetail?.isVerified ?? false;

  const formattedDate = createdAt
    ? new Date(createdAt).toLocaleDateString("es-CO", {
      year: "numeric",
      month: "long",
      day: "numeric",
    })
    : "";

  if (isLoading) {
    return (
      <div className="min-h-screen bg-[#FDFDFD]">
        <Navbar />
        <div className="flex flex-col items-center justify-center min-h-screen gap-6">
          <div className="relative">
            <div className="h-20 w-20 rounded-full border-[6px] border-gray-100 border-t-black animate-[spin_0.8s_linear_infinite]" />
            <div className="absolute inset-0 flex items-center justify-center">
              <ShoppingBag className="h-6 w-6 animate-bounce text-black" />
            </div>
          </div>
          <p className="text-xl font-bold text-[#0A0A0A] tracking-tight">
            Cargando producto...
          </p>
        </div>
      </div>
    );
  }

  if (!isDetailLoading && !detail && !stateProduct) {
    return (
      <div className="min-h-screen bg-[#FDFDFD]">
        <Navbar />
        <div className="flex flex-col items-center justify-center min-h-screen gap-6 px-4">
          <div className="h-24 w-24 bg-gray-100 rounded-[24px] flex items-center justify-center">
            <Package className="h-10 w-10 text-gray-400" />
          </div>
          <h2 className="text-2xl font-black text-[#0A0A0A]">
            Producto no encontrado
          </h2>
          <Link to="/productos">
            <Button className="rounded-2xl h-12 px-8 font-bold">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Volver al catálogo
            </Button>
          </Link>
        </div>
      </div>
    );
  }

  const colorConfig = COLOR_STYLE[color] ?? COLOR_STYLE["OTHER"];

  return (
    <div className="min-h-screen bg-[#FDFDFD] selection:bg-black selection:text-white">
      <Navbar />

      <div className="pt-28 max-w-[1400px] mx-auto px-4 sm:px-6 xl:px-12 pb-20">
        {/* ── Breadcrumb ── */}
        <Link
          to="/productos"
          className="inline-flex items-center gap-2 text-sm font-semibold text-[#5F6670] hover:text-[#0A0A0A] transition-colors mb-10 group"
        >
          <ArrowLeft className="h-4 w-4 transition-transform group-hover:-translate-x-1" />
          Volver al catálogo
        </Link>

        {/* ── Main Layout ── */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 xl:gap-20">
          {/* ══════════ LEFT COLUMN: Image Gallery ══════════ */}
          <div className="space-y-4">
            {/* Main image */}
            <div className="relative aspect-[4/5] rounded-[32px] overflow-hidden bg-[#F8F9FA] group">
              {images.length > 0 ? (
                <>
                  <img
                    src={images[currentImage]}
                    alt={name}
                    className="h-full w-full object-cover transition-transform duration-700 group-hover:scale-[1.02]"
                  />

                  {/* Nav arrows */}
                  {images.length > 1 && (
                    <div className="absolute inset-0 flex items-center justify-between px-4">
                      <button
                        onClick={() =>
                          setCurrentImage(
                            (p) => (p - 1 + images.length) % images.length
                          )
                        }
                        className="p-3 rounded-full bg-white/90 backdrop-blur-md text-black hover:bg-white transition-all shadow-lg hover:scale-110 active:scale-95"
                      >
                        <ChevronLeft className="h-5 w-5" />
                      </button>
                      <button
                        onClick={() =>
                          setCurrentImage((p) => (p + 1) % images.length)
                        }
                        className="p-3 rounded-full bg-white/90 backdrop-blur-md text-black hover:bg-white transition-all shadow-lg hover:scale-110 active:scale-95"
                      >
                        <ChevronRight className="h-5 w-5" />
                      </button>
                    </div>
                  )}

                  {/* Dots */}
                  {images.length > 1 && (
                    <div className="absolute bottom-5 left-1/2 -translate-x-1/2 flex gap-2 px-3 py-2 rounded-full bg-black/15 backdrop-blur-md">
                      {images.map((_, idx) => (
                        <button
                          key={idx}
                          onClick={() => setCurrentImage(idx)}
                          className={`rounded-full transition-all duration-300 ${idx === currentImage
                            ? "w-6 h-2 bg-white"
                            : "w-2 h-2 bg-white/60 hover:bg-white/90"
                            }`}
                        />
                      ))}
                    </div>
                  )}

                  {/* Out of stock badge */}
                  {!available && (
                    <div className="absolute top-5 left-5">
                      <div className="px-4 py-2 bg-red-500/90 text-white text-xs font-bold tracking-[0.15em] rounded-full shadow-lg backdrop-blur-sm">
                        AGOTADO
                      </div>
                    </div>
                  )}

                  {/* Favorite button (decorative) */}
                  <button className="absolute top-5 right-5 p-3 rounded-full bg-white/90 backdrop-blur-md text-[#5F6670] hover:text-red-500 transition-all duration-300 shadow-md hover:scale-110 group/fav">
                    <Heart className="h-5 w-5 transition-colors group-hover/fav:fill-red-500" />
                  </button>
                </>
              ) : (
                <div className="flex h-full w-full flex-col items-center justify-center text-[#9CA3AF] gap-4">
                  <ImageIcon className="h-16 w-16 stroke-1" />
                  <span className="text-sm font-medium">Sin imágenes</span>
                </div>
              )}
            </div>

            {/* Thumbnail strip */}
            {images.length > 1 && (
              <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-hide">
                {images.map((url, idx) => (
                  <button
                    key={idx}
                    onClick={() => setCurrentImage(idx)}
                    className={`relative shrink-0 h-20 w-20 rounded-[16px] overflow-hidden border-2 transition-all duration-200 ${idx === currentImage
                      ? "border-black scale-[1.05] shadow-md"
                      : "border-transparent opacity-60 hover:opacity-100"
                      }`}
                  >
                    <img
                      src={url}
                      alt={`${name} ${idx + 1}`}
                      className="h-full w-full object-cover"
                    />
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* ══════════ RIGHT COLUMN: Product Info ══════════ */}
          <div className="space-y-8 animate-in fade-in slide-in-from-right-4 duration-500">
            {/* Category & Type breadcrumb */}
            <div className="flex items-center gap-2 text-xs font-semibold text-[#9CA3AF] uppercase tracking-wider">
              <span>{productTypeName}</span>
              {category && (
                <>
                  <span className="h-1 w-1 rounded-full bg-gray-300" />
                  <span>{t(category)}</span>
                </>
              )}
            </div>

            {/* Title & Rating */}
            <div className="space-y-3">
              <h1 className="text-4xl xl:text-5xl font-black tracking-tighter text-[#0A0A0A] leading-[1.05] capitalize">
                {name.toLowerCase()}
              </h1>
              <div className="flex items-center gap-3 flex-wrap">
                <div className="flex items-center gap-2">
                  <StarRating value={rating} />
                  <span className="text-sm font-bold text-[#0A0A0A]">
                    {rating ? rating.toFixed(1) : "0.0"}
                  </span>
                  <span className="text-sm text-[#9CA3AF] font-medium">
                    calificación
                  </span>
                </div>
                <div
                  className={`px-3 py-1 rounded-full text-xs font-bold ${available
                    ? "bg-green-100 text-green-700"
                    : "bg-red-100 text-red-600"
                    }`}
                >
                  {available ? "Disponible" : "Agotado"}
                </div>
              </div>
            </div>

            {/* Price */}
            <div className="py-5 px-6 bg-[#0A0A0A] rounded-[24px] flex items-center justify-between">
              <span className="text-white/60 text-sm font-semibold">
                Precio
              </span>
              <span className="text-3xl font-black text-white tracking-tight">
                ${price.toLocaleString("es-CO")}
              </span>
            </div>

            {/* Description */}
            <div className="space-y-3">
              <h2 className="text-xs font-black uppercase tracking-widest text-[#9CA3AF]">
                Descripción
              </h2>
              <p className="text-[#3D3D3D] leading-relaxed font-medium text-[15px]">
                {description}
              </p>
            </div>

            {/* Specs chips */}
            <div className="space-y-3">
              <h2 className="text-xs font-black uppercase tracking-widest text-[#9CA3AF]">
                Especificaciones
              </h2>
              <div className="grid grid-cols-3 gap-3">
                {/* Fit */}
                <div className="flex flex-col gap-1.5 p-4 bg-white rounded-[20px] border border-gray-100 shadow-sm">
                  <div className="flex items-center gap-1.5 text-[#9CA3AF]">
                    <Ruler className="h-4 w-4" />
                    <span className="text-[10px] font-black uppercase tracking-widest">
                      Fit
                    </span>
                  </div>
                  <span className="text-sm font-bold text-[#0A0A0A] capitalize">
                    {t(generalFit)}
                  </span>
                </div>
                {/* Gender */}
                <div className="flex flex-col gap-1.5 p-4 bg-white rounded-[20px] border border-gray-100 shadow-sm">
                  <div className="flex items-center gap-1.5 text-[#9CA3AF]">
                    <Users className="h-4 w-4" />
                    <span className="text-[10px] font-black uppercase tracking-widest">
                      Género
                    </span>
                  </div>
                  <span className="text-sm font-bold text-[#0A0A0A] capitalize">
                    {t(gender)}
                  </span>
                </div>
                {/* Color */}
                <div className="flex flex-col gap-1.5 p-4 bg-white rounded-[20px] border border-gray-100 shadow-sm">
                  <div className="flex items-center gap-1.5 text-[#9CA3AF]">
                    <Palette className="h-4 w-4" />
                    <span className="text-[10px] font-black uppercase tracking-widest">
                      Color
                    </span>
                  </div>
                  <div className="flex items-center gap-2">
                    <div
                      className={`h-4 w-4 rounded-full shadow-inner flex-shrink-0 ${colorConfig.bg}`}
                      style={colorConfig.style}
                    />
                    <span className="text-sm font-bold text-[#0A0A0A] capitalize">
                      {t(color)}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            {/* Tags grouped by type */}
            {Object.keys(tagsByType).length > 0 && (
              <div className="space-y-3">
                <div className="flex items-center gap-2 text-[#9CA3AF]">
                  <Tag className="h-4 w-4" />
                  <h2 className="text-xs font-black uppercase tracking-widest">
                    Tags
                  </h2>
                </div>
                <div className="space-y-2">
                  {Object.entries(tagsByType).map(([type, tags]) => {
                    const style = TAG_TYPE_STYLES[type] ?? defaultTagStyle;
                    return (
                      <div
                        key={type}
                        className={`p-3 rounded-[18px] ${style.group}`}
                      >
                        <p className="text-[10px] font-black uppercase tracking-widest text-[#9CA3AF] mb-2">
                          {style.label}
                        </p>
                        <div className="flex flex-wrap gap-2">
                          {tags.map((tag) => (
                            <span
                              key={tag.id}
                              className={`px-3 py-1.5 rounded-full text-xs font-bold capitalize ${style.pill}`}
                            >
                              {tag.name.toLowerCase()}
                            </span>
                          ))}
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}

            {/* CTA + publication date */}
            <div className="flex flex-col sm:flex-row items-start sm:items-center gap-4">
              {linkProduct && (
                <a
                  href={linkProduct}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2 h-14 px-8 bg-[#0A0A0A] hover:bg-black text-white font-bold rounded-[20px] shadow-lg hover:shadow-xl transition-all hover:scale-[1.02] active:scale-[0.98] text-sm"
                >
                  Comprar en tienda oficial
                  <ExternalLink className="h-4 w-4" />
                </a>
              )}
              {formattedDate && (
                <div className="flex items-center gap-2 text-[#9CA3AF]">
                  <CalendarDays className="h-4 w-4 flex-shrink-0" />
                  <span className="text-xs font-semibold">
                    Publicado el {formattedDate}
                  </span>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* ══════════ BRAND SECTION ══════════ */}
        {brandName && (
          <div className="mt-16">
            <div className="flex items-center gap-2 mb-6">
              <h2 className="text-2xl font-black tracking-tight text-[#0A0A0A]">
                Marca
              </h2>
            </div>
            <div
              onClick={() => brandId && navigate(`/marcas/${brandId}`)}
              className="inline-flex items-center gap-5 p-6 bg-white rounded-[28px] border border-gray-100 shadow-sm cursor-pointer hover:shadow-md hover:border-blue-200 transition-all duration-300 group"
              role="button"
              title={`Ver perfil de ${brandName}`}
            >
              {/* Avatar */}
              <div className="relative">
                <div className="h-16 w-16 rounded-full overflow-hidden bg-[#0A0A0A] border-2 border-white shadow-md transition-transform group-hover:scale-105">
                  {brandPictureUrl ? (
                    <img
                      src={brandPictureUrl}
                      alt={brandName}
                      className="h-full w-full object-cover"
                    />
                  ) : (
                    <div className="h-full w-full flex items-center justify-center text-white text-xl font-black">
                      {brandName ? brandName.charAt(0).toUpperCase() : <Building2 className="h-8 w-8" />}
                    </div>
                  )}
                </div>
                {brandIsVerified && (
                  <VerifiedBadge size="sm" className="absolute -bottom-1 -right-1 z-20" />
                )}
              </div>
              {/* Info */}
              <div className="flex flex-col min-w-0 flex-1">
                <div className="flex items-center gap-1.5 min-w-0">
                  <span className="text-lg font-black text-[#0A0A0A] group-hover:text-blue-700 transition-colors capitalize truncate">
                    {brandName?.toLowerCase() || "Cargando..."}
                  </span>
                  {!!brandIsVerified && (
                    <VerifiedBadge size="md" className="flex-shrink-0 z-20" />
                  )}
                </div>
                <span className="text-xs font-semibold text-[#9CA3AF]">
                  {brandIsVerified ? "Marca verificada" : "Marca en Fashtoll"}
                </span>
              </div>
              <div className="ml-auto text-[#9CA3AF] group-hover:text-blue-600 group-hover:translate-x-1 transition-all">
                <ChevronRight className="h-5 w-5" />
              </div>
            </div>
          </div>
        )}

        {/* ══════════ REVIEWS SECTION (Decorativa) ══════════ */}
        <div className="mt-16 space-y-8">
          <div className="flex items-center gap-3">
            <div className="h-10 w-10 bg-black rounded-[14px] flex items-center justify-center text-white">
              <MessageSquare className="h-5 w-5" />
            </div>
            <h2 className="text-2xl font-black tracking-tight text-[#0A0A0A]">
              Reseñas
            </h2>
          </div>

          {/* Rating summary */}
          <div className="flex flex-col sm:flex-row gap-8 items-start sm:items-center p-8 bg-white rounded-[32px] border border-gray-100 shadow-sm">
            <div className="text-center">
              <p className="text-7xl font-black text-[#0A0A0A] tracking-tighter leading-none">
                {rating ? rating.toFixed(1) : "—"}
              </p>
              <StarRating value={rating ?? 0} size="lg" />
              <p className="text-xs font-bold text-[#9CA3AF] mt-2 uppercase tracking-widest">
                Puntuación general
              </p>
            </div>
            <div className="flex-1 space-y-2 w-full">
              {[5, 4, 3, 2, 1].map((stars) => (
                <div key={stars} className="flex items-center gap-3">
                  <span className="text-xs font-bold text-[#5F6670] w-4 text-right">
                    {stars}
                  </span>
                  <Star className="h-3.5 w-3.5 fill-yellow-400 text-yellow-400 flex-shrink-0" />
                  <div className="flex-1 h-2 bg-gray-100 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-yellow-400 rounded-full transition-all"
                      style={{
                        width: stars === 5 ? "65%" : stars === 4 ? "20%" : stars === 3 ? "10%" : "5%",
                      }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Notice: reviews not yet implemented */}
          <div className="flex items-start gap-3 p-4 bg-blue-50 rounded-[18px] border border-blue-100">
            <AlertCircle className="h-4 w-4 text-blue-400 flex-shrink-0 mt-0.5" />
            <p className="text-xs font-semibold text-blue-600">
              Las reseñas individuales estarán disponibles próximamente. Lo que ves a continuación es una vista previa del diseño.
            </p>
          </div>

          {/* Decorative review cards */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {FAKE_REVIEWS.map((review) => (
              <div
                key={review.id}
                className="p-6 bg-white rounded-[24px] border border-gray-100 shadow-sm space-y-4"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-10 w-10 rounded-full bg-[#0A0A0A] flex items-center justify-center text-white text-sm font-black">
                      {review.avatar}
                    </div>
                    <div>
                      <p className="text-sm font-bold text-[#0A0A0A]">
                        {review.author}
                      </p>
                      <p className="text-[10px] font-medium text-[#9CA3AF]">
                        {review.date}
                      </p>
                    </div>
                  </div>
                  <StarRating value={review.rating} />
                </div>
                <p className="text-sm text-[#5F6670] leading-relaxed font-medium">
                  {review.text}
                </p>
              </div>
            ))}
          </div>
        </div>
      </div>

      <style>{`
        .scrollbar-hide::-webkit-scrollbar { display: none; }
        .scrollbar-hide { -ms-overflow-style: none; scrollbar-width: none; }
      `}</style>
    </div>
  );
}
