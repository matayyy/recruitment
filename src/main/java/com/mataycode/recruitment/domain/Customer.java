package com.mataycode.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "profile_image_id_unique",
                        columnNames = "profile_image_id"
                )
        }
)
public class Customer {

    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender Gender;

    @Column(nullable = false)
    private String password;

    private String profileImageId;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String provider;

    @Column(nullable = false)
    private boolean enabled;

    //CONSTRUCTORS
    public Customer() {
    }

    public Customer(String name, String email, Gender gender, String password, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.Gender = gender;
        this.password = password;
        this.birthDate = birthDate;
        //SET DEFAULT VALUES
        this.roles = List.of(Role.USER);
        this.enabled = false;
    }

    //OWN METHODS
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    public int getAge() {
        return birthDate != null ? Period.between(birthDate, LocalDate.now()).getDays() : 0;
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Gender getGender() {
        return Gender;
    }

    public void setGender(Gender gender) {
        Gender = gender;
    }

    public String getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
