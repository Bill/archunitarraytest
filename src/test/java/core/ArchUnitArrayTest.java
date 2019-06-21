package core;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;


import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

class ArchUnitArrayTest {

  public static void main(String[] args) {
    final JavaClasses importedClasses = new ClassFileImporter().importPackages("core.sub");

    final ArchRule rule = classes().that().haveSimpleName("Bar")
        .should().onlyDependOnClassesThat(
            resideInAPackage("core.sub")
                .or(resideInAPackage("java.lang"))

                /*
                 make an exception to the rule: it's ok to depend on Foo

                 due to an apparent bug in ArchUnit, this results in an exception:

Exception in thread "main" java.lang.AssertionError: Architecture Violation [Priority: MEDIUM] - Rule 'classes that have simple name 'Bar' should only depend on classes that reside in a package 'core.sub' or reside in a package 'java.lang' or type core.Foo' was violated (1 times):
Method <core.sub.Bar.baz()> has return type <[Lcore.Foo;> in (Bar.java:0)
	at com.tngtech.archunit.lang.ArchRule$Assertions.assertNoViolation(ArchRule.java:91)
	at com.tngtech.archunit.lang.ArchRule$Assertions.check(ArchRule.java:81)
	at com.tngtech.archunit.lang.ArchRule$Factory$SimpleArchRule.check(ArchRule.java:195)
	at com.tngtech.archunit.lang.syntax.ObjectsShouldInternal.check(ObjectsShouldInternal.java:81)
	at core.ArchUnitArrayTest.main(ArchUnitArrayTest.java:22)

                 if you go over to Bar and eliminate the Foo[] reference by changing the baz()
                 signature to:

                   static Foo baz()

                 then ArchUnit will throw no exception

                 */

                // this is a workaround to the problem!
                //.or(type(Foo[].class))

                .or(type(Foo.class)));
    rule.check(importedClasses);
  }
}