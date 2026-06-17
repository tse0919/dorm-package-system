package com.dormitory.packagecollection.repository;

import com.dormitory.packagecollection.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    List<Package> findByStudentId(String studentId);

    List<Package> findByStatus(String status);
}
