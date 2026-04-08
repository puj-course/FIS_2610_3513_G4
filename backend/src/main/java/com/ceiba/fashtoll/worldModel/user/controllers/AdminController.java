package com.ceiba.fashtoll.worldModel.user.controllers;

import com.ceiba.fashtoll.worldModel.brand.BrandService;
import com.ceiba.fashtoll.worldModel.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BrandService brandService;

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
}
