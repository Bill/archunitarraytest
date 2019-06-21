# ArchUnit type Predicate Problem When Class Used in Array Declaration 

ArchUnit's `type` predicate doesn't seem to match a class used in an array declaration, when that `type` predicate is used inside an `or` predicate, to define an exception to some global condition.


Class [Bar.java](src/main/java/core/sub/Bar.java) has a method with this signature:

```java
  static Foo[] baz()
```

Our rule over in [ArchUnitArrayTest.java](src/test/java/core/ArchUnitArrayTest.java) says `Bar` can depend on classes in its own package or `java.lang` or:

```java
.or(type(Foo.class))
```

We expect the test to pass. But it fails with:

```
Exception in thread "main" java.lang.AssertionError: Architecture Violation [Priority: MEDIUM] - Rule 'classes that have simple name 'Bar' should only depend on classes that reside in a package 'core.sub' or reside in a package 'java.lang' or type core.Foo' was violated (1 times):
Method <core.sub.Bar.baz()> has return type <[Lcore.Foo;> in (Bar.java:0)
	at com.tngtech.archunit.lang.ArchRule$Assertions.assertNoViolation(ArchRule.java:91)
	at com.tngtech.archunit.lang.ArchRule$Assertions.check(ArchRule.java:81)
	at com.tngtech.archunit.lang.ArchRule$Factory$SimpleArchRule.check(ArchRule.java:195)
	at com.tngtech.archunit.lang.syntax.ObjectsShouldInternal.check(ObjectsShouldInternal.java:81)
	at core.ArchUnitArrayTest.main(ArchUnitArrayTest.java:22)
```

if you go over to [Bar.java](src/main/java/core/sub/Bar.java) and eliminate the `Foo[]` reference by changing the `baz()` signature to:

```java
static Foo baz()
```

&hellip;then ArchUnit will throw no exception.

Array declarations, seem to work in other situations. See, for instance, the definition of `baz2()`:

```java
static Quux[] baz2()
```

That signature generates no exception. ArchUnit recognizes that the array type is derived from `Quux` and that `Quux` resides in the `core.sub` package.

