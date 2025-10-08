package com.spring.st.student_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeeAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "fee_structure_id")
    private FeeStructure feeStructure;

    private double totalAmount;
    private double discount;
    private double netAmount;
    private double amount;

    // Getters and Setters

    @Override
    public String toString() {
        String studentDetails = (student != null) ? "Student[id=" + student.getId() + ", name=" + student.getFirstName() + "]" : "No Student";
        String feeStructureDetails = (feeStructure != null) ? "FeeStructure[id=" + feeStructure.getId() + ", feeType=" + feeStructure.getFeeType() + "]" : "No Fee Structure";

        return "FeeAllocation [id=" + id 
                + ", student=" + studentDetails 
                + ", feeStructure=" + feeStructureDetails 
                + ", totalAmount=" + totalAmount 
                + ", discount=" + discount 
                + ", netAmount=" + netAmount 
                + ", amount=" + amount + "]";
    }

}
