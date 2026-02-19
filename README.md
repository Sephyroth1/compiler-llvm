# Syntexa
## A programming language with a llvm backend

- This is my first programming language written with an llvm backend
- This programming language features the following syntax

### Variable Declaration
```sh
let x = val;
```

### Variable Assignment
```sh
x = val;
```

### If condition
```sh
if(cond) {
  // Statements
}
```

### While Loop and For Loop
```sh
while(cond) {
  // Statements
}

// For Loop
for(expr; expr; expr) {
// Statements
}
```

### Match (Switch case)
```sh
match (expr) {
case expr -> // Statements
...
}
```

### Functions
```sh
fn name() {
  //Statements
}
```

### Importing modules
```sh
import module
```



### Operators

- Unary
  - Minus (- operator)
  - ex: -23
- Binary
  - Plus
  - Subtract
  - Divide
  - Multiply
  - Modulus
  - Or
  - And
  - Xor
  - Equal
  - Not Equal
- Ternary
  - The Ternary Condition Operator (?)
 
### Features Added
- Basic Operations Added
- Semantic Analysis Phase Almost done

### Features to be added
- A lot more features to be added, Stay Tuned!

### Execution

1. Git clone this repository (Or Fork it for changes)
```sh
git clone https://github.com/Sephroth1/compiler-llvm.git
```

2. go to the root directory of the project
```sh
cd compiler-llvm/Syntexa
```

3. Build the project
```sh
./gradlew build
```

4. Create a file and write your simple program
```sh
touch input.txt
```
```sh
let a = 5
let b = 6
a + b
```

5. compile the program
```sh
./gradlew runLLVM -Pfile="input.txt"
```

### Note: You need to have llvm installed on your machine, at the very least you need clangd for this
### Note: This language can only compile basic programs, Not even a return instruction yet
