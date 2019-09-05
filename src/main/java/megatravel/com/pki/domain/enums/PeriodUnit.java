package megatravel.com.pki.domain.enums;

public enum PeriodUnit {
    DAY("D"),
    MONTH("M"),
    YEAR("Y");

    private final String name;

    PeriodUnit(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

    public static PeriodUnit factory(String name) {
        if (name.equals("D")) {
            return DAY;
        } else if (name.equals("M")) {
            return MONTH;
        } else {
            return YEAR;
        }
    }
}
