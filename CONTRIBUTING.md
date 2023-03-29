# Contributing to AdvancedAuth

Advanced Auth follows a fairly specific code design pattern. Try not to break the imposed pattern. Before doing PR please check locally that your changes work and do not break any other feature or mechanic.

## Commit guide

All commit messages must be in lowercase and must have a prefix indicating the type of the modification.

### Feature

The **feature** prefix will be used to add new features to the project:
`feature: this is a new generic feature`
`feature(lang): added ES language`
`feature(cache): added redis cache support`
`feature(storage): added mysql database support`

### Fix

The **fix** prefix will be used to fix a feature:
`fix: login message not showing`
`fix: mysql driver doesn't work`
`fix: fixes issue #123`

### Docs

The **docs** prefix to add or correct documentation.
`docs: added new commands to the documentation`
`docs(typo): correct spelling of documentation`

### Refactor

The **refactor** prefix is used when re-doing from scratch or re-implementing an existing feature. With intentions of improving its code, its performance or its quality.
`refactor: reimplemented command system`

### Test

The **test** prefix is used to create automated tests.
`test: improve unit testing`

### Others

The **chore** prefix is used to modify any file other than CI, source code, or tests.  
The **style** prefix will be used to reformat code. As well as correction of tabs and spaces.

## Code style

The code style must have 4 indentation spaces. Package import groups must be separated and the opening braces in methods must be on the same line as the arguments.

```java
package com.myapp;

import a.a.a;
import a.a.b;
import a.b.c;

import b.a.a;
import b.c.d;

import c.d.a;

public class MyApplication {
    private String value;
    private int anotherValue;

    public MyApplication() {
        this.value = value;
        this.anotherValue = anotherValue;
    }

    public void start() throws ApplicationException {
        this.value = "hello world";
    }
}
```
