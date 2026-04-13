import { Link } from "react-router-dom";
import { Star, Users, ChevronRight, Building2 } from "lucide-react";
import { VerifiedBadge } from "./VerifiedBadge";
import type { BrandPublic } from "../../services/brandPublicService";

interface BrandCardProps {
  brand: BrandPublic;
}

export function BrandCard({ brand }: BrandCardProps) {
  return (
    <Link 
      to={`/marcas/${brand.id}`}
      className="group block relative p-6 bg-white/40 backdrop-blur-md rounded-[32px] border border-[#E5E7EB] hover:border-black/10 hover:shadow-[0_20px_50px_rgb(0,0,0,0.08)] transition-all duration-500 hover:-translate-y-1 animate-in fade-in zoom-in"
    >
      <div className="flex items-center gap-5">
        {/* Profile Picture */}
        <div className="relative">
          <div className="h-20 w-20 rounded-full overflow-hidden bg-[#F3F4F6] border-2 border-white shadow-sm transition-transform duration-500 group-hover:scale-110">
            {brand.pictureUrl ? (
              <img 
                src={brand.pictureUrl} 
                alt={brand.name} 
                className="h-full w-full object-cover"
              />
            ) : (
              <div className="h-full w-full flex items-center justify-center text-[#9CA3AF]">
                <Building2 className="h-8 w-8 stroke-1" />
              </div>
            )}
          </div>
          {brand.isVerified && (
            <VerifiedBadge size="sm" className="absolute -bottom-1 -right-1 border-2 border-white rounded-full bg-white shadow-sm" />
          )}
        </div>

        {/* Brand Info */}
        <div className="flex-1 space-y-2">
          <div className="flex items-center justify-between gap-2 min-w-0">
            <div className="flex items-center gap-2 min-w-0 flex-1">
              <h3 className="text-xl font-black tracking-tight text-[#0A0A0A] group-hover:text-blue-600 transition-colors truncate capitalize flex-1 min-w-0">
                {brand.name.toLowerCase()}
              </h3>
              {brand.isVerified && <VerifiedBadge size="sm" className="flex-shrink-0" />}
            </div>
            <ChevronRight className="h-5 w-5 text-[#9CA3AF] group-hover:text-[#0A0A0A] group-hover:translate-x-1 transition-all flex-shrink-0" />
          </div>

          <div className="flex items-center gap-4">
            {/* Rating */}
            <div className="flex items-center gap-1">
              <Star className="h-4 w-4 fill-yellow-400 text-yellow-400" />
              <span className="text-sm font-bold text-[#0A0A0A]">
                {brand.rating ? brand.rating.toFixed(1) : "0.0"}
              </span>
            </div>

            {/* Followers */}
            <div className="flex items-center gap-1.5 text-[#5F6670]">
              <Users className="h-4 w-4" />
              <span className="text-xs font-semibold tabular-nums">
                {brand.followers?.toLocaleString() || 0}
              </span>
            </div>
          </div>
        </div>
      </div>
    </Link>
  );
}
