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

入力がi個、出力がo個の関数fを`i:o`と表し、
関数fおよびgを連結した関数を`f, g`と表す。

```
i:o = a:b, c:d
```

|条件|i|o|
|-|-|-|
|b == c|a|d|
|b < c|a + c - b|d|
|b > c|a|d + b - c|

```
0:1 = 0:1
0:1, 0:1 = 0:2 (1 1 -> 1 1)
0:1, 2:1 = 1:1 (1 [2 +] -> 3)
1:1, 2:1 = 2:1 (1 4 [sqrt +] -> 3)
```
