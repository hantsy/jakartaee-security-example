package com.example;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Enforces the DDD layered architecture:
 *
 * <pre>
 * interfaces      (REST + JSF)  -- may use application, infrastructure, domain
 * application     (use cases)   -- may only use domain
 * infrastructure  (JPA, security) -- may only use domain
 * domain          (model, repository, events) -- independent
 * </pre>
 */
class LayeredArchitectureTest {

    private static final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages(
                    "com.example.domain",
                    "com.example.application",
                    "com.example.infrastructure",
                    "com.example.interfaces");

    @Test
    void domain_must_not_depend_on_other_layers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..", "..interfaces..")
                .as("the domain layer must not depend on application, infrastructure or interfaces");
        rule.check(importedClasses);
    }

    @Test
    void application_must_only_depend_on_domain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..infrastructure..", "..interfaces..")
                .as("the application layer may only depend on the domain layer");
        rule.check(importedClasses);
    }

    @Test
    void infrastructure_must_only_depend_on_domain() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..interfaces..")
                .as("the infrastructure layer may only depend on the domain layer");
        rule.check(importedClasses);
    }

    @Test
    void application_and_interfaces_must_not_depend_on_jpa() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage("..application..", "..interfaces..")
                .should().dependOnClassesThat()
                .resideInAPackage("jakarta.persistence..")
                .as("application and interfaces must not depend on JPA; persistence concerns belong to domain and infrastructure");
        rule.check(importedClasses);
    }

    @Test
    void domain_repository_must_contain_only_interfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.repository..")
                .should().beInterfaces()
                .as("the domain repository package should only contain interfaces; implementations live in infrastructure");
        rule.check(importedClasses);
    }
}
