import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { Product } from "../../services/searchService";
import { Card, CardContent } from "../ui/card";
import { Star, ChevronLeft, ChevronRight, Heart, ImageIcon } from "lucide-react";
import { VerifiedBadge } from "./VerifiedBadge";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  const navigate = useNavigate();
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const images = product.imageUrls && product.imageUrls.length > 0 ? product.imageUrls : [];
  const hasMultipleImages = images.length > 1;

  const nextImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
    setCurrentImageIndex((prev) => (prev + 1) % images.length);
  };

  const prevImage = (e: React.MouseEvent) => {
    e.stopPropagation();
    e.preventDefault();
    setCurrentImageIndex((prev) => (prev - 1 + images.length) % images.length);
  };

  return (
    <div
      onClick={() => navigate(`/productos/${product.id}`, { state: { product } })}
      className="block h-full cursor-pointer"
    >
    <Card className="group relative overflow-hidden rounded-[32px] border-none bg-white/40 backdrop-blur-sm shadow-[0_8px_30px_rgb(0,0,0,0.04)] hover:shadow-[0_20px_50px_rgb(0,0,0,0.1)] transition-all duration-500 animate-fade-in group/card h-full flex flex-col hover:-translate-y-1">
      {/* Carrusel de Imágenes */}
      <div className="relative aspect-[4/5] overflow-hidden bg-[#F8F9FA]">
        <div className={`h-full w-full transition-all duration-500 ${!product.available ? 'opacity-70 grayscale-[0.3]' : ''}`}>
          {images.length > 0 ? (
            <>
              <img
                src={images[currentImageIndex]}
                alt={product.name}
                className="h-full w-full object-cover transition-transform duration-700 group-hover/card:scale-105"
              />
              {hasMultipleImages && (
                <div className="absolute inset-0 flex items-center justify-between px-3 opacity-0 group-hover/card:opacity-100 transition-opacity duration-300">
                  <button
                    onClick={prevImage}
                    className="p-1.5 rounded-full bg-white/90 backdrop-blur-md text-black hover:bg-white transition-all shadow-md hover:scale-110 active:scale-95"
                  >
                    <ChevronLeft className="h-4 w-4" />
                  </button>
                  <button
                    onClick={nextImage}
                    className="p-1.5 rounded-full bg-white/90 backdrop-blur-md text-black hover:bg-white transition-all shadow-md hover:scale-110 active:scale-95"
                  >
                    <ChevronRight className="h-4 w-4" />
                  </button>
                </div>
              )}
              {/* Indicadores */}
              {hasMultipleImages && (
                <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex gap-1.5 px-2.5 py-1.5 rounded-full bg-black/10 backdrop-blur-md">
                  {images.map((_, idx) => (
                    <div
                      key={idx}
                      className={`h-1.5 rounded-full transition-all duration-300 ${
                        idx === currentImageIndex ? "w-5 bg-white" : "w-1.5 bg-white/60"
                      }`}
                    />
                  ))}
                </div>
              )}
            </>
          ) : (
            <div className="flex h-full w-full flex-col items-center justify-center text-[#9CA3AF] gap-3">
              <ImageIcon className="h-12 w-12 stroke-1" />
              <span className="text-[11px] font-medium tracking-wide">Sin imágenes</span>
            </div>
          )}
        </div>

        {/* Badge de "Sin Stock" - No bloquea clics */}
        {!product.available && (
          <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
            <div className="px-5 py-2.5 bg-red-500/90 text-white text-[11px] font-bold tracking-[0.2em] rounded-full shadow-lg animate-pulse">
              Agotado
            </div>
          </div>
        )}

        {/* Botón Favoritos (Decorativo) */}
        <button className="absolute top-4 right-4 p-2.5 rounded-full bg-white/90 backdrop-blur-md text-[#5F6670] hover:text-red-500 transition-all duration-300 shadow-sm hover:scale-110 group/fav">
          <Heart className="h-4 w-4 transition-colors group-hover/fav:fill-red-500" />
        </button>
      </div>

      <CardContent className="p-5 flex-1 flex flex-col justify-between space-y-4">
        <div className="space-y-3">
          <div className="space-y-1">
            <div className="flex items-start justify-between gap-2">
              <h3 className="font-bold text-[#0A0A0A] text-lg leading-tight line-clamp-1 tracking-tight capitalize">
                {product.name.toLowerCase()}
              </h3>
              {/* Rating vuelve a su posición original */}
              <div className="flex items-center gap-1 shrink-0 bg-gray-50 px-2 py-1 rounded-lg">
                <Star className="h-3 w-3 fill-yellow-400 text-yellow-400" />
                <span className="text-[11px] font-bold text-[#0A0A0A]">{product.rating ? product.rating.toFixed(1) : "0"}</span>
              </div>
            </div>
            
            <p className="text-[11px] text-[#5F6670] font-medium capitalize flex items-center gap-1.5">
              {product.productTypeName.toLowerCase()} 
              <span className="h-0.5 w-0.5 rounded-full bg-gray-400" /> 
              {product.category.toLowerCase()}
            </p>
          </div>

          {/* Tags */}
          <div className="flex flex-wrap gap-1.5">
            {product.tags && product.tags.slice(0, 3).map((tag, idx) => (
              <span key={idx} className="text-[10px] font-medium text-[#5F6670] bg-[#F3F4F6] px-2 py-0.5 rounded-md capitalize">
                {tag.toLowerCase()}
              </span>
            ))}
            {product.tags && product.tags.length > 3 && (
              <span className="text-[10px] font-medium text-[#9CA3AF] px-1 self-center">
                +{product.tags.length - 3}
              </span>
            )}
          </div>
        </div>

        {/* Brand Info & Price */}
        <div className="flex items-center justify-between mt-auto">
          <div 
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/marcas/${product.brandId}`);
            }}
            className="flex items-center gap-2 group/brand hover:bg-gray-100 p-1.5 pr-3 rounded-full transition-all min-w-0 max-w-[72%]"
            title={`Ver perfil de ${product.brandName}`}
          >
            <div className="relative">
              <div className="h-6 w-6 rounded-full overflow-hidden bg-[#0A0A0A] border border-white shadow-sm transition-transform group-hover/brand:scale-110">
                {product.brandPictureUrl ? (
                  <img 
                    src={product.brandPictureUrl} 
                    alt={product.brandName} 
                    className="h-full w-full object-cover"
                  />
                ) : (
                  <div className="flex h-full w-full items-center justify-center text-[10px] font-black text-white">
                    {product.brandName?.charAt(0).toUpperCase()}
                  </div>
                )}
              </div>
              {product.brandIsVerified && (
                <VerifiedBadge size="xs" className="absolute -bottom-0.5 -right-0.5" />
              )}
            </div>
            <div className="flex items-center gap-1 min-w-0">
              <span className="text-[10px] font-black uppercase tracking-wider text-[#0A0A0A] truncate">
                {product.brandName}
              </span>
              {!!product.brandIsVerified && (
                <VerifiedBadge size="sm" className="flex-shrink-0" />
              )}
            </div>
          </div>
          
          {/* Price Badge */}
          <div className="px-4 py-1.5 bg-[#0A0A0A] rounded-full shadow-lg transform transition-transform group-hover/card:scale-110">
            <span className="text-sm font-black text-white italic">
              ${product.price ? product.price.toLocaleString("es-CO") : "0"}
            </span>
          </div>
        </div>
      </CardContent>
    </Card>
    </div>
  );
}
