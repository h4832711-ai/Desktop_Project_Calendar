package date;

public class DateTest
{
    public static void main(String[] args)
    {
        Date date1 = new Date(4, 11, 2026);
        Date date2 = new Date(4, 11, 2026);
        Date date3 = new Date(5, 11, 2026);

        // Test equals()
        System.out.println("date1 equals date2: "
                + date1.equals(date2));

        System.out.println("date1 equals date3: "
                + date1.equals(date3));

        // Test hashCode()
        System.out.println("Same hashCode: "
                + (date1.hashCode() == date2.hashCode()));
    }
}