package charts.tokens;

public class CityToken {
    private String cityName;
    private int countStudent;

    public CityToken(String cityName, int countStudent) {
        this.cityName = cityName;
        this.countStudent = countStudent;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCountStudent() {
        return countStudent;
    }

    @Override
    public String toString() {
        return "CityStatistic{" +
                "cityName='" + cityName + '\'' +
                ", countStudent=" + countStudent +
                '}';
    }
}
