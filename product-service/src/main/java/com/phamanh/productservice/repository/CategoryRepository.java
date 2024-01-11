package com.phamanh.productservice.repository;


import com.phamanh.productservice.domains.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Category findByCategoryName(String name);


    @Query("Select c from Category c Where c.name=:categoryName And c.parentCategory.name=:parentCategoryName")
    public Category findByNameAndParent(@Param("categoryName") String categoryName, @Param("parentCategoryName") String parentCategoryName);
}
