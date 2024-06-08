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
## checker

||1|2|3|結果|
|-|-|-|-|-|
|式|1|2|+|3|
|入出力|0:1|0:1|2:1|0:1|
|スタックサイズ|0|1|2|1|
>>>>>>> 900bb4ead03f2554e46c1d915b894fd4a7a45c77
