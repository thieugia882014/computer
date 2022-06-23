package aptech.computer.util;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringHelper {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public static String toSlug(String str,String id){
        str+="-"+id;
        String noWhiteSpace = WHITESPACE.matcher(str).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace,Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
    public static LocalDateTime toDateTime(String str){
        LocalDate ld = LocalDate.parse(str, DATEFORMATTER);
        return LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());
    }
}
