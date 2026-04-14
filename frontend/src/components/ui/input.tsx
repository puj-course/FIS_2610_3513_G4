import * as React from "react"
import { cn } from "../../lib/utils"


export type InputProps = React.InputHTMLAttributes<HTMLInputElement>

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ className, type, ...props }, ref) => {
    return (
      <input
        type={type}
        className={cn(
          "flex h-12 w-full rounded-2xl border border-[#E5E7EB] bg-[#F3F4F6] px-4 py-2 text-sm text-[#0A0A0A] placeholder:text-[#6B7280] focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-[#AEB4BC] disabled:cursor-not-allowed disabled:opacity-50",
          className
        )}
        ref={ref}
        {...props}
      />
    )
  }
)
Input.displayName = "Input"

export { Input }
