import { BadgeCheck } from "lucide-react";

interface VerifiedBadgeProps {
  size?: "xs" | "sm" | "md" | "lg" | "xl";
  className?: string;
}

export function VerifiedBadge({ size = "md", className = "" }: VerifiedBadgeProps) {
  const sizeMap = {
    xs: "h-4 w-4",
    sm: "h-4.5 w-4.5",
    md: "h-5 w-5",
    lg: "h-7 w-7",
    xl: "h-10 w-10",
  };

  return (
    <div className={`inline-flex items-center justify-center flex-shrink-0 z-10 ${className}`}>
      <BadgeCheck 
        className={`${sizeMap[size]} text-white fill-[#38BDF8] stroke-[2.5px] drop-shadow-sm`}
        aria-label="Cuenta verificada"
      />
    </div>
  );
}
