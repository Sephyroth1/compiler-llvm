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

4. Run the project
```sh
./gradlew run
```
### Note: If you want to give a separate input, go to the src/main/java/org/example/App.java and Edit the main function's String input
