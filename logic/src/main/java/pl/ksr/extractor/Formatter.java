package pl.ksr.extractor;

public class Formatter {


    public static String removeTags(String s) {
        return s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"));
    }

    public static String splitTags(String s) {
        s = s.replace("<D>", "");
        s = s.replace("</D>", "");
        return s;
    }

    public static String[] extractTitle(String s) {
        String[] array = new String[0];
        if (s.contains("<TITLE>")) {
            array = extract(s.toLowerCase(),"<title>", "</title>");
        }
        return array;
    }

    public static String[] extractBody(String s) {
        String[] array = new String[0];
        if (s.contains("<BODY>")) {
            array = extract(s, "<BODY>", "</BODY>");
        }
        return array;
    }

    private static String [] extract(String s, String startTag, String endTag) {
        String[] array = s.split(startTag);
        array = array[1].split(endTag);
        String text = array[0].replaceAll(",", " ");
        text = text.replaceAll("\\.", " ");
        array = text.split("\\s+");
        return array;
    }
}