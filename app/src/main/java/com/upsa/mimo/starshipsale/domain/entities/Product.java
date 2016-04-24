package com.upsa.mimo.starshipsale.domain.entities;

public class Product {

    private final Long id;
    private final String name;
    private final String description;
    private final String image;
    private final Double price;
    private final Boolean isFavorite;
    private final Boolean isFeatured;
    private final Boolean isAddedToCart;

    private Product(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        image = builder.image;
        price = builder.price;
        isFavorite = builder.isFavorite;
        isFeatured = builder.isFeatured;
        isAddedToCart = builder.isAddedToCart;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Product copy) {
        Builder builder = new Builder();
        builder.id = copy.id;
        builder.name = copy.name;
        builder.description = copy.description;
        builder.image = copy.image;
        builder.price = copy.price;
        builder.isFavorite = copy.isFavorite;
        builder.isFeatured = copy.isFeatured;
        builder.isAddedToCart = copy.isAddedToCart;
        return builder;
    }

    public String getName() {
        return name;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public static final class Builder {
        private Long id = 1L;
        private String name;
        private String description;
        private String image;
        private Double price;
        private Boolean isFavorite = Boolean.FALSE;
        private Boolean isFeatured = Boolean.FALSE;
        private Boolean isAddedToCart = Boolean.FALSE;

        private Builder() {
        }

        public Builder withId(Long val) {
            id = val;
            return this;
        }

        public Builder withName(String val) {
            name = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withImage(String val) {
            image = val;
            return this;
        }

        public Builder withPrice(Double val) {
            price = val;
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

        public Product build() {
            return new Product(this);
        }
    }
}
