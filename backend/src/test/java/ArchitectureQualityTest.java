import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchitectureQualityTest {

    @Test
    public void controllers_should_only_call_services() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ceiba.fashtoll");

        ArchRule rule = classes()
                .that().resideInAPackage("..controllers..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..services..",
                        "..dtos..",
                        "..dto..",
                        "..utilities..",
                        "..exceptionHandling..",
                        "java..",
                        "org.springframework..",
                        "jakarta.." // Crucial: Limpia los 6 errores de @Valid en el log
                );

        rule.check(importedClasses);
    }
}