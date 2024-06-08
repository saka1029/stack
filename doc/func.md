# func

## syntax

```
statement  = func | expression
func       = 'func' [{ID} ':' {ID}] '->' expression
expression = atom | number | list | quote
atom       = ID
qutoe      = '¥'' expression
list       = '(' {expression} ')'
```

## func

```
0:1 = 0:1
0:1, 0:1 = 0:2
0:1, 2:1 = 1:1 (1 [2 +] -> 3)
1:1, 2:1 = 1:1 (1 4 [sqrt +] -> 3)
```
