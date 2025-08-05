import * as React from "react"
import { Check, Search } from "lucide-react"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"
import { IMapOptions } from "@/queries"


 
interface IAutoCompleteProps {
    options: IMapOptions[];
    inputValue: string;
    setInputValue: (inputValue: string) => void;
    onValueSelect: (value: IMapOptions) => void;
}

export function AutoComplete({options, inputValue, setInputValue, onValueSelect}: IAutoCompleteProps) {
  const [open, setOpen] = React.useState(false)
  const [value, setValue] = React.useState("")

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-[200px] justify-between"
        >
          {value}
          <Search className="opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[200px] p-0">
        <Command shouldFilter={false}>
          <CommandInput placeholder="Search Room..." className="h-9" value={inputValue} onChangeCapture={(e)=>{
             setInputValue(e.currentTarget.value)
          }} />
          <CommandList>
            <CommandEmpty>No Location Found</CommandEmpty>
            <CommandGroup>
              {options.map((option) => (
                <CommandItem
                  key={option.id}
                  value={option.room}
                  onSelect={(currentValue) => {
                    console.log("currentValue", currentValue)
                    setValue(currentValue === value ? "" : currentValue)
                    setOpen(false)
                    onValueSelect(option)
                  }}
                >
                  {option.room}
                  <Check
                    className={cn(
                      "ml-auto",
                      value === option.room ? "opacity-100" : "opacity-0"
                    )}
                  />
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  )
}
