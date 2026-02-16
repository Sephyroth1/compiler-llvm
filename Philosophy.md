# Philosophy of my Language
## Syntexa
### Introduction
- Syntexa is a programming language that is designed with only one goal in mind: simplicity if you want it to be and complexity if you need it to be, with a focus on low level control and aggresively eliminating boilerplate code.
- This language is for the people who two kinds of people: those who want to write code quickly and those who want control over the machine they are coding on.
- This language is aligned with one other purpose, deliberate and specific change, all variables are immutable by default.
- Null allowed: Yes, Syntexa supports null values.
- Gradual Static Typing: Syntexa supports gradual static typing, allowing you to choose when and where to enforce type safety.
### The main property: Inference-first and Annotation-first typing?
- Inference-first and Annotation-first typing: Syntexa supports both inference-first and annotation-first typing, allowing you to choose the approach that best fits your needs.
#### For Example

```sh
# Inference first typing
let x = 10;
# Annotation first typing
let y: ConstraintsList? = 20;
```

### One other experimental feature I'm working on is to let the developer choose the specific constraints of each data type they are applying to a variable.

### For Example
```sh
let x : checked = 10;
# Checked checks for integer overflow and returns immediately
let x = unchecked = 10;
# UnChecked checks for integer overflow and wraps around to 0
```

# AST Design (Basic)

## Expressions
  - ### Literal
  - ### Variable
  - ### BinaryOp
  - ### UnaryOp
  - ### Block

## Statements
  - ### Assignment
  - ### If
  - ### While
  - ### For
  - ### Return

## Declarations
  - ### Function
  - ### Constraints

## Constraints

- For Each Variable, we can define a unique constraint associated with it.
- So for example, we can define a constraints for integer and say that it can be mutable or checked or unchecked or a special keyword called "exact" where it doesn't automatically widen the size of the datatype used and returns an error if the value is out of range.


### Integer Constraints

- Mutable: Allows the value of the variable to be changed.
- Checked: Checks for integer overflow and returns immediately.
- Unchecked: Checks for integer overflow and wraps around to 0.
- Exact: Doesn't automatically widen the size of the datatype used and returns an error if the value is out of range.
- Signed: Allows negative values.
- Small: Doesn't allow values larger than a certain size.
- Medium: default size for integers.
- Large: Large integer size constraints
- Huge: Huge integer size constraints

### Character Constraints
(Characters and Strings are by default immutable like everything else and can't be mutable, new instances is created for modifications and their values possible are the normal unicode characters)

### Floating Point Constraints

- Mutable: Allows the value of the variable to be changed.
- Checked: Checks for floating point overflow and returns immediately.
- Unchecked: Checks for floating point overflow and wraps around to 0.
- Exact: Doesn't automatically widen the size of the datatype used and returns an error if the value is out of range.
- Signed: Allows negative values.
- Large: Large floating point size constraints
- Precise: Floating point size constraints with high precision

### Boolean Constraints
(True, False, Maybe)
  - True: Represents a boolean value of true.
  - False: Represents a boolean value of false.
  - Maybe: Represents a boolean value of indeterminateness (requires explicit resolution when used in control flow, like you can't do x > 0 since x can be true or false, you have to do (x > 0) == true, since Maybe condition can be true or false).

- Mutable: Allows the value of the variable to be changed.


# Basic Grammar (LL Friendly)

```Grammar
Program        -> (Decl | Stmt)*
Decl           -> 'let' IDENT ConstraintList? '=' Expr ';'
FuncDecl       -> 'func' IDENT '(' Params ')' Block
Stmt
  -> IfStmt
  | WhileStmt
  | ForStmt
  | ReturnStmt
  | BreakStmt
  | ContinueStmt
  | AssignmentStmt
  | ExprStmt
AssignmentStmt -> IDENT '=' Expr ';'
ExprStmt       -> Expr ';'
IfStmt         -> 'if' Expr Block ('else' Block)?
WhileStmt      -> 'while' Expr Block
ForStmt        -> 'for' '(' Expr ';' Expr ';' Expr ')' Block
ReturnStmt     -> 'return' Expr? ';'
Block          -> '{' BlockItem* '}'
BlockItem      -> Stmt | ExprStmt
```


# DataTypes allowed:
- ## Integer
- ## Floating Point (Double and Float)
- ## String
- ## Char
- ## Boolean

# Maybe
## Let’s classify conditions clearly
### Conditions that are never Maybe
#### These are the vast majority in normal code.

```sh
let x = 10;
if x > 5 { ... }        // Bool
if 3 == 4 { ... }       // Bool
if True { ... }         // Bool
```

**Why?**

**All operands are known**

**No null**

**No unchecked operations**

**No unresolved constraints**

These evaluate to True or False, never Maybe.

Conditions that can become Maybe
Only when uncertainty enters.

### Example A

```sh
A — null
let x = null;
x > 5        // Maybe
```

Because:

Comparison with absence is not false,
It is unknown

### Example B

**unchecked**

```sh
let x: unchecked = input();
x > 5 // Maybe
```

Because:

input() is not statically known

unchecked arithmetic may overflow

result is not provably correct

### Example C

**gradual typing**

```sh
let x;        // type not yet known
x > 5         // Maybe
```


Because:

type constraints insufficient

compiler cannot decide

Conditions that resolve Maybe

Resolution is explicit and intentional.

```sh
if maybeCond == True { ... }
```

This comparison:

asks a definite question,
produces a Bool.

It is safe for control flow

So Maybe does not propagate forever.

# Basic Syntax

## Variable Declaration & Assignment

```sh
// Declaration
let x = 5;
// Not Assignable
x = 10; // Error: Cannot assign to immutable variable

// Assignment
// Only Works if variables is mutable
let x : mut = 5;
x = 10;
```

## Control Flow

### If/If else

```cpp
let x = 10
// Conditions are only evaluated if they are completely deterministic and without any ambiguity, if they are not, they are evaluated as Maybe

// Bool
if x > 5 {
    println("x is greater than 5");
} else { 
    println("x is less than or equal to 5");
}

let x = input()

// Maybe and it requires explicit Resolution
if x > 5 {} 
// It is evaluted correctly now
if (x > 5) == true {
    println("x is greater than 5")
} else {
    println("x is less than or equal to 5")
}
```

## While

```cpp
while cond {
    // Code block
}
// That is the basic syntax of a while loop, the condition should be evaluatable without ambiguity otherwise Maybe, just like If
```

## For loop

```cpp
for (Expr; Expr; Expr) {
    // Code block
}

// That is the basic syntax of a for loop, the condition should be evaluatable without ambiguity otherwise Maybe, just like If
```

## Match (Switch)

```cpp
match x {
    case 1 => println("One");
    case 2 => println("Two");
    case _ => println("Other");
}
// case _ is the default case
```

## Functions

```cpp
fn fnName() -> TypeReturn {
   // Code Blocks 
   return;
}

// Also planning to add higher order functions too with some constraints maybe like lambda functions are higher order functions

let val = () -> { return val;}
```

### Important Note: This langugage will not have && or ||, but will have 'or' and 'and' keyword like python
