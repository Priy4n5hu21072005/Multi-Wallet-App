package com.example.multiwallet.entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Full Name is required")
    @Size(max = 100, message = "maximum character 100")
    @Column(nullable = false,length = 100)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter the Valid Mail")
    @Size(max = 254,message = "The Mail Size is 254 character")
    @Column(nullable = false,unique = true,length = 254)
    private String email;

    @NotBlank(message = "Password should not blank")
    @Size(max = 60,message = "Maximum character is 60")
    @Column(nullable = false, length = 60)
    private String password;

    @Size(max=15)
    @Column(unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<Wallet> wallets;

    // No Argments Constructer
    public User(){}

    // Getter
    public UUID getId(){return id;}
    public String getFullName(){return fullName;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getPhoneNumber(){return phoneNumber;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}

    // Setter
    public void setId(UUID id){this.id = id;}
    public void setFullName(String fullName){this.fullName=fullName;}
    public void setEmail(String email){this.email=email;}
    public void setPassword(String password){this.password=password;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
    public void setUpdatedAt(LocalDateTime updatedAt){this.updatedAt=updatedAt;}

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt =LocalDateTime.now();
    }
}
