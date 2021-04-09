package de.shadowsoft.greenLicense.common.crypto;


public class GeneratorConfig {

    private long min;
    private long max;
    private boolean includeAzUpper;
    private boolean includeAzLower;
    private boolean includeNumbers;
    private boolean includeSpecial;
    private boolean includeBrackets;
    private String exclude;
    private String include;

    public GeneratorConfig() {
        min = 6;
        max = 12;
        includeAzUpper = true;
        includeAzLower = true;
        includeNumbers = true;
        includeSpecial = false;
        includeBrackets = false;
        exclude = "";
        include = "";
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public boolean isIncludeAzUpper() {
        return includeAzUpper;
    }

    public void setIncludeAzUpper(boolean includeAzUpper) {
        this.includeAzUpper = includeAzUpper;
    }

    public boolean isIncludeAzLower() {
        return includeAzLower;
    }

    public void setIncludeAzLower(boolean includeAzLower) {
        this.includeAzLower = includeAzLower;
    }

    public boolean isIncludeNumbers() {
        return includeNumbers;
    }

    public void setIncludeNumbers(boolean includeNumbers) {
        this.includeNumbers = includeNumbers;
    }

    public boolean isIncludeSpecial() {
        return includeSpecial;
    }

    public void setIncludeSpecial(boolean includeSpecial) {
        this.includeSpecial = includeSpecial;
    }

    public boolean isIncludeBrackets() {
        return includeBrackets;
    }

    public void setIncludeBrackets(boolean includeBrackets) {
        this.includeBrackets = includeBrackets;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }
}
