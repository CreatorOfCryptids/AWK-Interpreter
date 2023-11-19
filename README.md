# AWK Interpreter
I made this AWK compiler as my ICSI 311 semester long project.

## Prossess
The program is split into three phases:
1. Lexing
2. Parsing
3. Interpreting

In the lexing phase, the awk source code is read into one single string and is grouped char by char into the correct key word or into the right classification (number, string literal, word, etc.). This generates a list of tokens that can be read in the next step.

In the parsing phase, token list is organized into an AST following the AWK grammar using recursive desent. 

Finally in the interpreting phase, the program walks thrught the generated AST to perform the program.

## Limitations
This program was done for a semester length class so it only implements the required functionality. As such there are a few places where this program might differ or lack functionality that a full AWK Interpreter might have.

These differences include:
- REGEX patterns are surounded by backticks (`) instead of slashes.
- Multidimentional arrays are not implemented (yet!).
- The only built in functions currently fully implemented are print, getline, next, gsub, index, length, match, split, sub, substr, tolower, and toupper.
