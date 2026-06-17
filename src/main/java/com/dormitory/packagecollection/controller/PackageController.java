package com.dormitory.packagecollection.controller;

import com.dormitory.packagecollection.entity.Package;
import com.dormitory.packagecollection.repository.PackageRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageRepository packageRepository;

    public PackageController(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @GetMapping
    public List<Package> listAll() {
        return packageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Package getById(@PathVariable Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到包裹：" + id));
    }

    @PostMapping
    public ResponseEntity<Package> createPackage(@Valid @RequestBody CreatePackageRequest request) {
        Package pkg = new Package();
        pkg.setStudentId(request.studentId());
        pkg.setStudentName(request.studentName());
        pkg.setItemName(request.itemName());
        pkg.setPackageType(request.packageType());
        pkg.setRemark(request.remark());
        pkg.setArrivalTime(request.arrivalTime() != null ? request.arrivalTime() : LocalDateTime.now());

        Package saved = packageRepository.save(pkg);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}/status")
    public Package updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到包裹：" + id));

        pkg.setStatus(request.status());
        return packageRepository.save(pkg);
    }

    @PutMapping("/{id}/pickup")
    public Package markAsPickedUp(@PathVariable Long id) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到包裹：" + id));

        pkg.setStatus("已領取");
        return packageRepository.save(pkg);
    }

    public record CreatePackageRequest(
            @NotBlank String studentId,
            @NotBlank String studentName,
            @NotBlank String itemName,
            @NotBlank String packageType,
            String remark,
            LocalDateTime arrivalTime
    ) {
    }

    public record UpdateStatusRequest(
            @NotBlank String status
    ) {
    }
}
