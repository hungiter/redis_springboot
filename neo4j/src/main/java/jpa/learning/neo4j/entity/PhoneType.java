package jpa.learning.neo4j.entity;

public enum PhoneType {
    // KaiOS models
    JioPhone_F120B(PhoneSystem.KaiOS),
    JioPhone_F220B(PhoneSystem.KaiOS),
    JioPhone_F300B(PhoneSystem.KaiOS),
    JioPhone_F310B(PhoneSystem.KaiOS),

    // Android models
    Samsung_Galaxy_S21(PhoneSystem.Android),
    Google_Pixel_6(PhoneSystem.Android),
    OnePlus_9(PhoneSystem.Android),
    Xiaomi_Mi_11(PhoneSystem.Android),
    Sony_Xperia_1_III(PhoneSystem.Android),

    // iOS models
    iPhone_13(PhoneSystem.iOS),
    iPhone_12(PhoneSystem.iOS),
    iPhone_11(PhoneSystem.iOS),
    iPhone_SE(PhoneSystem.iOS),
    iPhone_XR(PhoneSystem.iOS);

    private final PhoneSystem system;

    PhoneType(PhoneSystem system) {
        this.system = system;
    }

    public PhoneSystem getSystem() {
        return this.system;
    }
}