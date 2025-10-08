package com.spring.st.student_management.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "A", "B", etc.

    @ManyToMany(mappedBy = "sections")
    private List<StudentClass> studentClasses;

    @Override
    public String toString() {
        return "Section [id=" + id + ", name=" + name + ", studentClasses=" + studentClasses + "]";
    }


    
    // Getters and setters
}
