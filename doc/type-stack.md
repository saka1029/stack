## Type Stack

# 文法

```
type                = prime-type | variable-type | instruction-type | block-type
prime-type          = 'a' | 'b' | ... | 'z'
variable-type       = 'A' | 'B' | ... | 'Z'
instruction-type    = type-list '->' type-list 
type-list           = '(' { type } ')'
block-type          = '[' instruction-type ']'
```

# instruction types

```
dup = (T) -> (T T)
drop = (T) -> ()
swap = (T U) -> (U T)
over = (T U) -> (T U T)
```

## 算術式

```
+ = (i i) -> (i)
- = (i i) -> (i)
* = (i i) -> (i)
/ = (i i) -> (i)
```