package dto;

public enum TypePate {
    
    PATE_EPAISE("pate-epaisse"), 
    PATE_FINE("pate-fine"),
    PATE_MEDIUM("pate-medium");

    private String label;

    private TypePate(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    } 

    public static TypePate get(String typePateString) {
        for (TypePate pate : TypePate.values()) {
            if (pate.getLabel().equals(typePateString)) {
                return pate;
            }
        }
        return null;
    }
}
