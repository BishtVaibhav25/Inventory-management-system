package com.ims.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;



@Entity
@Table(name="categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Category {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="Category name is required")
	@Size(max=100,message="Category name must not exceed 100 characters")
	@Column(unique=true,nullable=false,length=100)
	private String name;
	
	 @Size(max = 255, message = "Description must not exceed 255 characters")
	 private String description;

}
