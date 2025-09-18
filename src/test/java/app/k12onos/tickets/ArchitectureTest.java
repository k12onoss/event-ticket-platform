package app.k12onos.tickets;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.Repository;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

class ArchitectureTest {

    static final String BASE_PACKAGE = "app.k12onos.tickets";

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

    // TODO Add your own rules and remove those that don't apply to your project

    @Test
    void domain_model_should_not_depend_on_application_services() {
        noClasses()
            .that()
            .resideInAPackage(BASE_PACKAGE + "..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage(BASE_PACKAGE + "..services..")
            .check(this.importedClasses);
    }

    @Test
    void domain_model_should_not_depend_on_the_user_interface() {
        noClasses()
            .that()
            .resideInAPackage(BASE_PACKAGE + "..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(BASE_PACKAGE + "..ui..")
            .check(this.importedClasses);
    }

    @Test
    void repositories_should_only_be_used_by_application_services_and_other_domain_classes() {
        classes()
            .that()
            .areAssignableTo(Repository.class)
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(BASE_PACKAGE + "..domain..", BASE_PACKAGE + "..services..")
            .check(this.importedClasses);
    }

    @Test
    void application_services_should_not_depend_on_the_user_interface() {
        noClasses()
            .that()
            .resideInAPackage(BASE_PACKAGE + "..services..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(BASE_PACKAGE + "..ui..")
            .check(this.importedClasses);
    }

    @Test
    void there_should_not_be_circular_dependencies_between_feature_packages() {
        var filteredClasses = this.importedClasses
            .that(
                new DescribedPredicate<JavaClass>(
                    "package name does not contain domain.entities, exceptions and base.ui") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        return !javaClass.getPackageName().contains("domain.entities")
                            && !javaClass.getPackageName().contains("exceptions")
                            && !javaClass.getPackageName().contains("base.ui")
                            && !javaClass.getPackageName().contains("base.utils");
                    }
                });
        slices().matching(BASE_PACKAGE + ".(*)..").should().beFreeOfCycles().check(filteredClasses);
    }

    @Test
    void security_package_should_not_depend_on_other_application_classes() {
        classes()
            .that()
            .resideInAPackage(BASE_PACKAGE + ".security..")
            .should()
            .onlyAccessClassesThat()
            .resideOutsideOfPackage(BASE_PACKAGE + "..")
            .orShould()
            .accessClassesThat()
            .resideInAPackage(BASE_PACKAGE + ".security..")
            .because("Security classes should only depend on external libraries and other security classes");
    }

}
