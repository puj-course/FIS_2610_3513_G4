import type { Product } from "../../services/searchService";

import { Card, CardContent } from "../ui/card";
import { Button } from "../ui/button";
import { ShoppingBag, Star } from "lucide-react";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  return (
    <Card className="group overflow-hidden rounded-[32px] border-[#E5E7EB] hover:shadow-2xl transition-all duration-500 animate-fade-in">
      <div className="relative aspect-[4/5] overflow-hidden bg-[#F3F4F6]">
        {product.pictureUrl ? (
          <img
            src={product.pictureUrl}
            alt={product.name}
            className="h-full w-full object-cover transition-transform duration-700 group-hover:scale-110"
          />
        ) : (
          <div className="flex h-full w-full items-center justify-center text-[#9CA3AF]">
            <ShoppingBag className="h-12 w-12" />
          </div>
        )}
        
        {/* Badge de Marca */}
        <div className="absolute top-4 left-4 px-3 py-1 bg-white/90 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest text-[#0A0A0A] shadow-sm">
          {product.brandName}
        </div>
      </div>

      <CardContent className="p-6 space-y-4">
        <div className="space-y-1">
          <div className="flex justify-between items-start">
            <h3 className="font-bold text-[#0A0A0A] line-clamp-1">{product.name}</h3>
            <div className="flex items-center gap-1 text-yellow-500">
              <Star className="h-3 w-3 fill-current" />
              <span className="text-[10px] font-bold">4.8</span>
            </div>
          </div>
          <p className="text-xs text-[#5F6670] font-semibold uppercase tracking-wider">
            {product.category} • {product.gender === 'M' ? 'Hombre' : product.gender === 'F' ? 'Mujer' : 'Unisex'}
          </p>
        </div>

        <div className="flex items-center justify-between pt-2">
          <span className="text-xl font-black text-[#0A0A0A]">
            ${product.price.toLocaleString('es-CO')}
          </span>
          <Button size="sm" className="rounded-xl h-10 w-10 p-0">
             <ShoppingBag className="h-5 w-5" />
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}
