package com.upsa.mimo.starshipsale.domain.entities;

import com.google.gson.annotations.SerializedName;

public class Product {


    @SerializedName("id") private final String id;
    @SerializedName("name") private final String name;
    @SerializedName("model") private final String model;
    @SerializedName("manufacturer") private final String manufacturer;
    @SerializedName("starship_class") private final String starshipClass;
    @SerializedName("image")private final String image;
    @SerializedName("cost_in_credits") private final String costInCredits;
    @SerializedName("crew") private final String crew;
    @SerializedName("cargo_capacity") private final String cargoCapacity;
    @SerializedName("is_favorite") private final Boolean isFavorite;
    @SerializedName("is_featured") private final Boolean isFeatured;
    @SerializedName("is_in_cart") private final Boolean isAddedToCart;
    @SerializedName("passengers") private final String passengers;
    @SerializedName("hyperdrive_rating") private final String hyperDriveRating;

    private final String description;

    public boolean isFeatured() {
        return isFeatured;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return costInCredits;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getStarshipClass() {
        return starshipClass;
    }

    public String getPassengers() {
        return passengers;
    }

    public String getCrew() {
        return crew;
    }

    public String getCargoCapacity() {
        return cargoCapacity;
    }

    public String getHyperDriveRating() {
        return hyperDriveRating;
    }

    public String getImage() {
        return image;
    }
    private Product(Builder builder) {
        id = builder.id;
        name = builder.name;
        model = builder.model;
        manufacturer = builder.manufacturer;
        starshipClass = builder.starshipClass;
        image = builder.image;
        costInCredits = builder.costInCredits;
        crew = builder.crew;
        cargoCapacity = builder.cargoCapacity;
        isFavorite = builder.isFavorite;
        isFeatured = builder.isFeatured;
        isAddedToCart = builder.isAddedToCart;
        passengers = builder.passengers;
        hyperDriveRating = builder.hyperDriveRating;
        description = builder.description;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Product copy) {
        Builder builder = new Builder();
        builder.id = copy.id;
        builder.name = copy.name;
        builder.model = copy.model;
        builder.manufacturer = copy.manufacturer;
        builder.starshipClass = copy.starshipClass;
        builder.image = copy.image;
        builder.costInCredits = copy.costInCredits;
        builder.crew = copy.crew;
        builder.cargoCapacity = copy.cargoCapacity;
        builder.isFavorite = copy.isFavorite;
        builder.isFeatured = copy.isFeatured;
        builder.isAddedToCart = copy.isAddedToCart;
        builder.passengers = copy.passengers;
        builder.hyperDriveRating = copy.hyperDriveRating;
        builder.description = copy.description;
        return builder;
    }


    public static final class Builder {
        private String id;
        private String name;
        private String model;
        private String manufacturer;
        private String starshipClass;
        private String image;
        private String costInCredits;
        private String crew;
        private String cargoCapacity;
        private Boolean isFavorite;
        private Boolean isFeatured;
        private Boolean isAddedToCart;
        private String passengers;
        private String hyperDriveRating;
        private String description;

        private Builder() {
        }

        public Builder withId(String val) {
            id = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withModel(String val) {
            model = val;
            return this;
        }

        public Builder withManufacturer(String val) {
            manufacturer = val;
            return this;
        }

        public Builder withStarshipClass(String val) {
            starshipClass = val;
            return this;
        }

        public Builder withImage(String val) {
            image = val;
            return this;
        }

        public Builder withCostInCredits(String val) {
            costInCredits = val;
            return this;
        }

        public Builder withCrew(String val) {
            crew = val;
            return this;
        }

        public Builder withCargoCapacity(String val) {
            cargoCapacity = val;
            return this;
        }

        public Builder withIsFavorite(Boolean val) {
            isFavorite = val;
            return this;
        }

        public Builder withIsFeatured(Boolean val) {
            isFeatured = val;
            return this;
        }

        public Builder withIsAddedToCart(Boolean val) {
            isAddedToCart = val;
            return this;
        }

        public Builder withPassengers(String val) {
            passengers = val;
            return this;
        }

        public Builder withHyperDriveRating(String val) {
            hyperDriveRating = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        return id.equals(product.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
