package com.pl.bas.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pl.bas.domain.enumeration.Form;

import com.pl.bas.domain.enumeration.Rating;

/**
 * A Drug.
 */
@Entity
@Table(name = "drug")
public class Drug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "form", nullable = false)
    private Form form;

    @NotNull
    @Column(name = "dose", nullable = false)
    private String dose;

    @NotNull
    @Column(name = "packaging", nullable = false)
    private String packaging;

    @NotNull
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private Rating rating;

    @Column(name = "description")
    private String description;

    @OneToOne    @JoinColumn(unique = true)
    private Drug substitute;

    @ManyToMany
    @JoinTable(name = "drug_producer",
               joinColumns = @JoinColumn(name = "drugs_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "producers_id", referencedColumnName = "id"))
    private Set<Producer> producers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "drug_diseases",
               joinColumns = @JoinColumn(name = "drugs_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "diseases_id", referencedColumnName = "id"))
    private Set<Disease> diseases = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favourite_drugs",
        joinColumns = @JoinColumn(name = "drug_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> usersWithFavourite = new HashSet<>();

    public Set<User> getUsersWithFavourite() {
        return usersWithFavourite;
    }

    public void setUsersWithFavourite(Set<User> usersWithFavourite) {
        this.usersWithFavourite = usersWithFavourite;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Drug name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Form getForm() {
        return form;
    }

    public Drug form(Form form) {
        this.form = form;
        return this;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getDose() {
        return dose;
    }

    public Drug dose(String dose) {
        this.dose = dose;
        return this;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getPackaging() {
        return packaging;
    }

    public Drug packaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Drug price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }

    public Drug rating(Rating rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public Drug description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drug getSubstitute() {
        return substitute;
    }

    public Drug substitute(Drug drug) {
        this.substitute = drug;
        return this;
    }

    public void setSubstitute(Drug drug) {
        this.substitute = drug;
    }

    public Set<Producer> getProducers() {
        return producers;
    }

    public Drug producers(Set<Producer> producers) {
        this.producers = producers;
        return this;
    }

    public Drug addProducer(Producer producer) {
        this.producers.add(producer);
        return this;
    }

    public Drug removeProducer(Producer producer) {
        this.producers.remove(producer);
        return this;
    }

    public void setProducers(Set<Producer> producers) {
        this.producers = producers;
    }

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public Drug diseases(Set<Disease> diseases) {
        this.diseases = diseases;
        return this;
    }

    public Drug addDiseases(Disease disease) {
        this.diseases.add(disease);
        disease.getDrugs().add(this);
        return this;
    }

    public Drug removeDiseases(Disease disease) {
        this.diseases.remove(disease);
        disease.getDrugs().remove(this);
        return this;
    }

    public void setDiseases(Set<Disease> diseases) {
        this.diseases = diseases;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Drug drug = (Drug) o;
        if (drug.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), drug.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Drug{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", form='" + getForm() + "'" +
            ", dose='" + getDose() + "'" +
            ", packaging='" + getPackaging() + "'" +
            ", price=" + getPrice() +
            ", rating='" + getRating() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
