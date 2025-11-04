# Coding Conventions

## Domain Language

- Use domain-specific language to make code more readable and maintainable.
- This includes jvminline value classes to abstract domain concepts, such as `UserId`, `OrderId`, etc.
- Immutablility is preferred, so use `val` instead of `var` where possible.
