package com.ceiba.fashtoll.worldModel.admin;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import com.ceiba.fashtoll.worldModel.admin.metrics.QualityMetricsTracker;
import com.ceiba.fashtoll.worldModel.brand.BrandService;
import com.ceiba.fashtoll.worldModel.user.UserService;
import com.tngtech.archunit.lang.EvaluationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BrandService brandService;
    private final QualityMetricsTracker metricsTracker;

    @PutMapping("/users/{id}/status")
    public ResponseEntity<Void> setUserStatus(@PathVariable Long id, @RequestParam boolean active) {
        userService.setUserActiveStatus(id, active);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/brands/{id}/verify")
    public ResponseEntity<Void> verifyBrand(@PathVariable Long id, @RequestParam boolean verified) {
        brandService.verifyBrand(id, verified);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quality/dashboard")
    public ResponseEntity<Map<String, Object>> getQualityDashboard() {

        Map<String, Object> response = Map.of(
                "Porcentaje de requests exitosas", String.format("%.2f%%", metricsTracker.getApiRobustnessIndex()),
                "Porcentaje promedio de palabras utiles del query de un usuario", String.format("%.2f%%", metricsTracker.getAverageQueryQuality()),
                "Total de transacciones exitosas", metricsTracker.getApiRobustnessIndex() == 100 && metricsTracker.getAverageQueryQuality() == 0 ? "Calculado en tiempo de ejecución" : metricsTracker.getSuccessfulRequestsCount(),
                "Porcentaje de arquitectura exitosa", String.format("%.2f%%", checkArchitectureCompliance())
        );

        return ResponseEntity.ok(response);
    }

    private double checkArchitectureCompliance() {
        try {
            // Escanea las clases del proyecto
            JavaClasses importedClasses = new ClassFileImporter().importPackages("com.ceiba.fashtoll");

            // Define la regla para atrapar violaciones arquitectonicas
            ArchRule controllersRule = classes()
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

            // Evalúa sin romper el flujo del backend
            EvaluationResult result = controllersRule.evaluate(importedClasses);
            int violationsCount = result.getFailureReport().getDetails().size();

            // Total de conexiones que tienen los controladores
            int totalControllerDependencies = 0;
            for (JavaClass javaClass : importedClasses) {
                if (javaClass.getPackageName().contains(".controllers")) {
                    totalControllerDependencies += javaClass.getDirectDependenciesFromSelf().size();
                }
            }

            if (totalControllerDependencies == 0) return 100.0;

            double complianceScore = ((double) (totalControllerDependencies - violationsCount) / totalControllerDependencies) * 100.0;

            // return Math.max(0.0, Math.min(100.0, complianceScore));
            return Math.clamp(complianceScore, 0.0, 100.0);

        } catch (Throwable t) {
            return 0.0;
        }
    }
}
