# Frame

## syntax

```
program    = {expression}
expression = SYMBOL | INT | quote | store | list
quote      = "'" expression
store      = "=" SYMBOL
list       = "(" [frame] {expression} ")"
frame      = "@" {SYMBOL} "->" {SYMBOL} {local} ":"
local      = "," SYMBOL expression
```
special tokens:
```
    "(", ")", ",", ":", "'", "@" ,"=", "->"
```

comment:
"{" any characters except '}' "}"