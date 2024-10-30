package ru.pas_zhukov.eventmanager.dto.request;

import jakarta.validation.constraints.*;

public class LocationRequestDto {

    @Null
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    private String address;

    @Min(5)
    @NotNull
    private Integer capacity;

    private String description;

    public LocationRequestDto() {}

    public LocationRequestDto(Long id, String name, String address, Integer capacity, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    public @Null Long getId() {
        return id;
    }

    public void setId(@Null Long id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 2, max = 50) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(min = 2, max = 50) String name) {
        this.name = name;
    }

    public @NotBlank String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank String address) {
        this.address = address;
    }

    public @Min(5) @NotNull Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(@Min(5) @NotNull Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
