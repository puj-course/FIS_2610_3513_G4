import * as React from "react"
import { cn } from "../../lib/utils"


export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "default" | "outline" | "ghost"
  size?: "default" | "sm" | "lg"
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant = "default", size = "default", ...props }, ref) => {
    const variants = {
      default: "bg-[#0A0A0A] text-white hover:bg-[#1A1A1A]",
      outline: "border border-[#E5E7EB] bg-white text-[#0A0A0A] hover:bg-[#F9FAFB]",
      ghost: "text-[#5F6670] hover:bg-[#F3F4F6] hover:text-[#0A0A0A]",
    }
    
    const sizes = {
      default: "h-11 px-6 py-2",
      sm: "h-9 px-4 text-xs",
      lg: "h-14 px-10 text-lg",
    }

    return (
      <button
        className={cn(
          "inline-flex items-center justify-center rounded-2xl text-sm font-semibold transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-[#AEB4BC] disabled:pointer-events-none disabled:opacity-50",
          variants[variant],
          sizes[size],
          className
        )}
        ref={ref}
        {...props}
      />
    )
  }
)
Button.displayName = "Button"

export { Button }
