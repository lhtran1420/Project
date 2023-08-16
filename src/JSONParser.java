import java.util.*;

public class JSONParser {
    private static int index;
    private static String json;

    public static Map<String, Object> parse(String jsonString) {
        index = 0;
        json = jsonString;
        return parseObject();
    }

    private static Map<String, Object> parseObject() {
        Map<String, Object> map = new HashMap<>();

        if (json.charAt(index) == '{') {
            index++; // Consume '{'
            consumeWhitespace();

            if (json.charAt(index) == '}') {
                index++; // Consume '}'
                return map; // Empty object
            }

            while (index < json.length()) {
                String key = parseString();
                consumeWhitespace();

                if (json.charAt(index) != ':') {
                    throw new RuntimeException("Expected ':'");
                }

                index++; // Consume ':'
                consumeWhitespace(); // Move this line here

                Object value = parseValue();
                map.put(key, value);

                consumeWhitespace();

                if (json.charAt(index) == '}') {
                    index++; // Consume '}'
                    break;
                }

                if (json.charAt(index) != ',') {
                    throw new RuntimeException("Expected ','");
                }

                index++; // Consume ','
                consumeWhitespace();
            }

            return map;
        } else {
            throw new RuntimeException("Expected '{'");
        }
    }


    private static String parseString() {
        StringBuilder builder = new StringBuilder();
        index++; // Consume '"'

        while (index < json.length() && json.charAt(index) != '"') {
            builder.append(json.charAt(index));
            index++;
        }

        index++; // Consume '"'
        return builder.toString();
    }

    private static Object parseValue() {
        consumeWhitespace();

        char ch = json.charAt(index);
        if (ch == '{') {
            return parseObject();
        } else if (ch == '[') {
            return parseArray();
        } else if (ch == '"') {
            return parseString();
        } else if (Character.isDigit(ch) || ch == '-') {
            return parseNumber();
        } else if (json.startsWith("true", index)) {
            index += 4; // Consume 'true'
            return true;
        } else if (json.startsWith("false", index)) {
            index += 5; // Consume 'false'
            return false;
        } else if (json.startsWith("null", index)) {
            index += 4; // Consume 'null'
            return null;
        } else {
            throw new RuntimeException("Invalid JSON value");
        }
    }

    private static List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        index++; // Consume '['
        consumeWhitespace();

        if (json.charAt(index) == ']') {
            index++;
            return list; // Empty array
        }

        while (index < json.length()) {
            Object value = parseValue();
            list.add(value);
            consumeWhitespace();

            if (json.charAt(index) == ']') {
                index++;
                break;
            }

            if (json.charAt(index) != ',') {
                throw new RuntimeException("Expected ','");
            }

            index++; // Consume ','
            consumeWhitespace();
        }

        return list;
    }

    private static void consumeWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

    private static double parseNumber() {
        int startIndex = index;
        while (index < json.length() && (Character.isDigit(json.charAt(index)) || json.charAt(index) == '-' || json.charAt(index) == '.')) {
            index++;
        }
        String numberStr = json.substring(startIndex, index);
        return Double.parseDouble(numberStr);
    }

    public static void main(String[] args) {
        String json = "{\"debug\": \"on\",\"window\": {\"title\": \"sample\",\"size\": 500}}";
        Map<String, Object> parsed = JSONParser.parse(json);
        System.out.println(parsed);

        Map<String, Object> window = (Map<String, Object>) parsed.get("window");
        assert window != null;

        String title = (String) window.get("title");
        assert title != null && title.equals("sample");

        Double size = (Double) window.get("size");
        assert size != null && size.equals(500);

        System.out.println(parsed.get("debug"));
        System.out.println();
        System.out.println(title);
        System.out.println(size);
    }
}