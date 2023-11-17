    package Data;

    public enum ProductCategory {
        BAG,
        BAKERY,
        BEVERAGES,
        BREAD,
        CANNED,
        CHILDREN,
        COFFEE_TEA,
        CONFECTIONERY,
        DAIRY,
        DRINKS,
        EGGS,
        FISH,
        FROZEN,
        FRUITS,
        GROCERY,
        HOUSEHOLD,
        HYGIENE,
        MEAT,
        PACKAGES,
        PET,
        READY,
        SAUCES_AND_SPICES,
        SEAFOOD,
        SNACKS,
        SWEETS,
        UNCATEGORIZED,
        UNDEFINED,
        VEGETABLES;
    }
//    public enum ProductCategory {
//        BAKERY("Bakery"),
//        BEVERAGES("Beverages"),
//        BREAD("Bread"),
//        CANNED("Canned"),
//        CHILDREN("Children"),
//        COFFEE_TEA("Coffee and Tea"),
//        CONFECTIONERY("Confectionery"),
//        DAIRY("Dairy"),
//        DRINKS("Drinks"),
//        EGGS("Eggs"),
//        FISH("Fish"),
//        FROZEN("Frozen"),
//        FRUITS("Fruits"),
//        GROCERY("Grocery"),
//        HOUSEHOLD("Household"),
//        HYGIENE("Hygiene"),
//        MEAT("Meat"),
//        PACKAGES("Packages"),
//        PET("Pet"),
//        READY("Ready"),
//        SAUCES_AND_SPICES("Sauces and Spices"),
//        SEAFOOD("Seafood"),
//        SNACKS("Snacks"),
//        SWEETS("Sweets"),
//        UNCATEGORIZED("Uncategorized"),
//        UNDEFINED("Undefined"),
//        VEGETABLES("Vegetables");
//
//        private final String displayName;
//
//        ProductCategory(String displayName) {
//            this.displayName = displayName;
//        }
//
//        public static ProductCategory parseString(final String parseString) {
//            Integer index = Integer.getInteger(parseString);
//            if (index != null)
//                return getCategoryByIndex(index);
//            return getCategoryByName(parseString);
//        }
//        public static ProductCategory getCategoryByName(final String displayName) {
//            return Arrays.stream(values())
//                    .filter(category -> category.getDisplayName().equalsIgnoreCase(displayName))
//                    .findFirst()
//                    .orElse(UNDEFINED);
//        }
//        public static ProductCategory getCategoryByIndex(final int index) {
//            if (index >= 0 && index < values().length) {
//                return values()[index];
//            }
//            return UNDEFINED;
//        }
//
//        public String getDisplayName() {
//            return displayName;
//        }
//
//        @Override
//        public String toString() {
//            return displayName;
//        }
//    }

