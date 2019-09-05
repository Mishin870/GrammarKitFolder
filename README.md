# GrammarKitFolder
Folding support for Intellij IDEA Grammar Kit BNF file type

### Example:

```
someRule ::= id*
//BLOCK someBlock
  class ::= modifiers 'class' id '{' classBody '}' {pin=1}
  method ::= modifiers type id '(' arguments ')' '{' methodBody '}'
//END
```

Will be turned into:

```
someRule ::= id*
[+]{someBlock}
```

### Setup
[Plugin page](https://plugins.jetbrains.com/plugin/12983-grammar-kit-folder)
