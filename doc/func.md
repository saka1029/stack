# func

## syntax

```
statement  = func | proc | expression
func       = 'func' {ID} ['->' {ID}] ':' expression
proc       = 'proc' {ID} ':' expression
expression = atom | number | list | quote
atom       = ID
qutoe      = '¥'' expression
list       = '(' {expression} ')'
```

## func

|No|戻り値の数|表記|
|-|-|-|
|1|0|`proc a0 a1 : BODY`|
|2|1|`func a0 a1 : BODY`|
|3|>=2|`func a0 a1 -> r0 r1 : BODY`|

## checker

||1|2|3|結果|
|-|-|-|-|-|
|式|1|2|+|3|
|入出力|0:1|0:1|2:1|0:1|
|スタックサイズ|0|1|2|1|
