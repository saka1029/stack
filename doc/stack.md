# stack

## append

### 最初の定義

```
/append
    (over () ==
        (swap drop)
        (over tail over append swap drop swap head swap pair)) define
```

then部分

```
() (1 2 3 4) append
-> () (1 2 3 4) swap
-> (1 2 3 4) () drop
-> (1 2 3 4)
```

else部分

```
(1 2) (3 4) append
-> (1 2) (3 4) over
-> (1 2) (3 4) (1 2) tail
-> (1 2) (3 4) (2) over
-> (1 2) (3 4) (2) (3 4) append
-> (1 2) (3 4) (2 3 4) swap
-> (1 2) (2 3 4) (3 4) drop
-> (1 2) (2 3 4) swap
-> (2 3 4) (1 2) head
-> (2 3 4) 1 swap
-> 1 (2 3 4) pair
-> (1 2 3 4)
```

### ２番目の定義

```
/append
    (over () ==
        (swap drop)
        (over tail swap append swap head swap pair)) define
```

then部分

```
() (1 2 3 4) append
-> () (1 2 3 4) swap
-> (1 2 3 4) () drop
-> (1 2 3 4)
```

else部分

```
(1 2) (3 4) append
-> (1 2) (3 4) over
-> (1 2) (3 4) (1 2) tail
-> (1 2) (3 4) (2) swap
-> (1 2) (2) (3 4) append
-> (1 2) (2 3 4) swap
-> (2 3 4) (1 2) head
-> (2 3 4) 1 swap
-> 1 (2 3 4) pair
-> (1 2 3 4)
```

### rotとunpairを使った定義

```
/append
    (swap dup () ==
        (drop)
        (unpair rot append pair)) define
```

then部分

```
() (1 2 3 4) append
-> () (1 2 3 4) swap
-> (1 2 3 4) () drop
-> (1 2 3 4)
```

else部分

```
(1 2) (3 4) append
-> (1 2) (3 4) swap
-> (3 4) (1 2) unpair
-> (3 4) 1 (2) rot
-> 1 (2) (3 4) append
-> 1 (2 3 4) pair
-> (1 2 3 4)
```

### rappendを使った定義

rappendは第２引数のリストの後に第１引数のリストを連結する。

```
(3 4) (1 2) rappend -> (1 2 3 4)
```

```
/rappend
    (dup () ==
        (drop)
        (unpair rot append pair)) define
/append
    (swap rappend) define
```

then部分

```
(1 2 3 4) () rappend
-> (1 2 3 4) () drop
-> (1 2 3 4)
```

else部分

```
(3 4) (1 2) rappend
-> (3 4) (1 2) unpair
-> (3 4) 1 (2) rot
-> 1 (2) (3 4) append
-> 1 (2 3 4) pair
-> (1 2 3 4)
```