import * as React from "react"
import { cn } from "../../lib/utils"


const Label = React.forwardRef<
  HTMLLabelElement,
  React.LabelHTMLAttributes<HTMLLabelElement>
>(({ className, ...props }, ref) => (
  <label
    ref={ref}
    className={cn(
      "text-sm font-semibold leading-none peer-disabled:cursor-not-allowed peer-opacity-70 text-[#0A0A0A]",
      className
    )}
    {...props}
  />
))
Label.displayName = "Label"

export { Label }
