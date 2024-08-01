import java.util.regex.Matcher
import java.util.regex.Pattern

String yearMonth = "5年"
String regex = "(\\d+)年(\\d+)月";
Pattern pattern = Pattern.compile(regex);
Matcher matcher = pattern.matcher(yearMonth);
if (matcher.find()) {
    println matcher.group(0)
    println matcher.group(1)
}