# AWK Compiler
I made this AWK compiler as my ICSI 311 semester long project.

The only modifications to the syntax are that the REGEX patterns are surounded by '`'s instead of slashes, and that it cannot handle multidimentional arrays (yet!).

## Prossess
The program is split into three phases:
1. Lexing
2. Parsing
3. Interpreting

In the lexing phase, the awk source code is read into one single string and is grouped char by char as the correct key word or into the right classification.

Next in the parsing phase, the source code is organized into an AST following the AWK grammar using recursive desent. 

Finally in the interpreting phase, (NOT YET COMPLEATED).
