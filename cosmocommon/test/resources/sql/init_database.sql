CREATE ALIAS ALTER_CUSTOM AS $$
String alterCustom(String value) {
    return "[ALTERED:" + value + " ]";
}
$$;;
